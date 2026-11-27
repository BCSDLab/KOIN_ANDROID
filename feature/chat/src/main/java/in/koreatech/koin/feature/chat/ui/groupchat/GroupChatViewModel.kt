package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_CONTEXT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_DOMAIN_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatRoomEvent
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.usecase.callvan.GetCallvanPostDetailUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatConnectionStateUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomInfoUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomsUseCase
import `in`.koreatech.koin.domain.usecase.chat.SubscribeChatRoomEventsUseCase
import `in`.koreatech.koin.domain.util.suspendRunCatching
import `in`.koreatech.koin.feature.chat.ui.SharedChatConnection
import `in`.koreatech.koin.feature.chat.ui.mapper.toChatDomainType
import `in`.koreatech.koin.feature.chat.ui.model.ChatImageUploadException
import `in`.koreatech.koin.feature.chat.ui.model.ChatMessageSession
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.hildan.krossbow.websocket.WebSocketConnectionException
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.syntax.simple.subIntent
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
@Suppress("LongParameterList", "TooManyFunctions")
class GroupChatViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val getRooms: GetChatRoomsUseCase,
    private val getChatRoomInfo: GetChatRoomInfoUseCase,
    private val getCallvanPostDetailUseCase: GetCallvanPostDetailUseCase,
    private val messageSession: ChatMessageSession,
    private val sharedConnection: SharedChatConnection,
    private val getConnectionState: GetChatConnectionStateUseCase,
    private val subscribeRoom: SubscribeChatRoomEventsUseCase
) : ViewModel(), ContainerHost<GroupChatState, GroupChatSideEffect> {
    private val domainType = ChatDomain.valueOf(checkNotNull(savedStateHandle[EXTRA_CHAT_DOMAIN_TYPE])).toChatDomainType()
    private val contextId: Int = checkNotNull(savedStateHandle[EXTRA_CHAT_CONTEXT_ID])
    private val legacyChatRoomId: Int? = savedStateHandle[EXTRA_CHAT_ROOM_ID]
    private var roomInfo: ChatRoomInfo? = null
    private val loadedRoomId = CompletableDeferred<String>()
    private var receivingMessagesJob: Job? = null

    override val container = container<GroupChatState, GroupChatSideEffect>(GroupChatState(domainType = domainType)) {
        observeMessages()
        loadRoom()
    }

    private fun observeMessages() = intent {
        repeatOnSubscription {
            launch { messageSession.observeCurrentUser() }
            messageSession.state.collect { sessionState ->
                reduce {
                    state.copy(
                        messages = sessionState.messages,
                        uploadingImages = sessionState.uploadingImages
                    )
                }
            }
        }
    }

    private fun loadRoom() = intent {
        if (domainType == ChatDomainType.CALLVAN) {
            val postDetail = getCallvanPostDetailUseCase(contextId).getOrElse {
                failLoadingRoom()
                return@intent
            }
            reduce {
                state.copy(
                    departure = postDetail.departure,
                    arrival = postDetail.arrival,
                    departureTime = postDetail.departureTime,
                    currentMemberCount = postDetail.currentParticipants,
                    maxMemberCount = postDetail.maxParticipants
                )
            }
        }
        getRoom().onSuccess { room ->
            roomInfo = room
            messageSession.bind(room)
            reduce {
                state.copy(
                    isLoading = false,
                    title = room.roomName,
                    currentMemberCount = room.memberCount ?: state.currentMemberCount,
                    maxMemberCount = room.maxMemberCount ?: state.maxMemberCount,
                    canSend = room.canSend,
                    readReceiptRoomId = room.roomId.takeIf { room.supportsReadReceipts }
                )
            }
            loadedRoomId.complete(room.roomId)
        }.onFailure {
            failLoadingRoom()
        }
    }

    private suspend fun getRoom(): Result<ChatRoomInfo> = suspendRunCatching {
        val room = getRooms(domainType).getOrThrow().singleOrNull {
            it.domainType == domainType && it.contextId == contextId &&
                (legacyChatRoomId == null || it.chatRoomId == legacyChatRoomId)
        } ?: throw KoinChatException.RoomNotFoundException()
        getChatRoomInfo(room.roomId).getOrThrow()
    }

    private fun refreshRoomInfo(roomId: String) = intent {
        getChatRoomInfo(roomId).onSuccess { room ->
            roomInfo = room
            messageSession.bind(room)
            reduce {
                state.copy(
                    title = room.roomName,
                    currentMemberCount = room.memberCount ?: state.currentMemberCount,
                    maxMemberCount = room.maxMemberCount ?: state.maxMemberCount,
                    canSend = room.canSend,
                    readReceiptRoomId = room.roomId.takeIf { room.supportsReadReceipts }
                )
            }
        }.onFailure { Timber.e(it) }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun refreshMessages(roomId: String) = subIntent {
        refreshRoomInfo(roomId)
        messageSession.loadHistory().onFailure {
            if (!messageSession.hasHistoryCursor && messageSession.state.value.messages.isEmpty()) {
                postSideEffect(GroupChatSideEffect.FailedToLoadMessages)
            }
        }
    }

    private fun failLoadingRoom() = intent {
        reduce { state.copy(isLoading = false) }
        postSideEffect(GroupChatSideEffect.FailedToLoadChatRoom)
    }

    fun startReceivingMessages() {
        if (receivingMessagesJob?.isActive == true) return
        receivingMessagesJob = intent {
            val roomId = loadedRoomId.await()
            coroutineScope {
                val owner = currentCoroutineContext().job
                val history = async { refreshMessages(roomId) }
                launch {
                    var previous: ChatConnectionState? = null
                    getConnectionState().collect { current ->
                        if (previous == ChatConnectionState.RECONNECTING && current == ChatConnectionState.CONNECTED) {
                            refreshMessages(roomId)
                        }
                        previous = current
                    }
                }
                flow<ChatRoomEvent> {
                    try {
                        sharedConnection.acquire(owner).onSuccess {
                            val events = subscribeRoom(roomId)
                            history.await()
                            refreshMessages(roomId)
                            emitAll(events)
                        }.onFailure {
                            Timber.e(it)
                            postSideEffect(GroupChatSideEffect.FailedToLoadMessages)
                        }
                    } finally {
                        sharedConnection.release(owner)
                    }
                }.retryWhen { error, _ ->
                    val shouldRetry = error is UnknownHostException ||
                        error is WebSocketConnectionException && error.cause is UnknownHostException
                    if (shouldRetry) delay(RECONNECT_DELAY_MS)
                    shouldRetry
                }.catch {
                    Timber.e(it)
                    postSideEffect(GroupChatSideEffect.FailedToLoadMessages)
                }.collect { event ->
                    when (event) {
                        is ChatRoomEvent.MessageCreated -> messageSession.accept(event.message)
                        is ChatRoomEvent.RoomChanged -> {
                            refreshRoomInfo(roomId)
                            messageSession.loadHistory()
                        }
                    }
                }
            }
        }
    }

    fun stopReceivingMessages() {
        receivingMessagesJob?.cancel()
        receivingMessagesJob = null
    }

    fun onChatInputValueChange(value: String) = blockingIntent {
        reduce { state.copy(chatInputValue = value) }
    }

    fun sendMessage() = intent {
        val room = roomInfo ?: return@intent
        val message = if (domainType == ChatDomainType.TEAM_RECRUITMENT) {
            state.chatInputValue.trim()
        } else {
            state.chatInputValue
        }
        if (message.isBlank() || !room.canSend) return@intent
        messageSession.send(message).onSuccess {
            reduce { state.copy(chatInputValue = "") }
        }.onFailure {
            postSideEffect(GroupChatSideEffect.FailedToSendMessage)
        }
    }

    fun uploadImage(fileSize: Long, fileType: String, fileName: String, imageUri: Uri) = intent {
        val room = roomInfo ?: return@intent
        if (!room.canSend) return@intent
        messageSession.upload(fileSize, fileType, fileName, imageUri).onFailure { error ->
            if (error is ChatImageUploadException) {
                postSideEffect(GroupChatSideEffect.FailedToUploadImage)
            } else {
                postSideEffect(GroupChatSideEffect.FailedToSendMessage)
            }
        }
    }

    private companion object {
        const val RECONNECT_DELAY_MS = 1000L
    }
}
