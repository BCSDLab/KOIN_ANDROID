package `in`.koreatech.koin.feature.chat.ui.chat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.core.navigation.utils.EXTRA_APPLICATION_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_CONTEXT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_DOMAIN_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentChatException
import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatRoomEvent
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.usecase.chat.ChatBlockUserUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatConnectionStateUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomInfoUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetChatRoomsUseCase
import `in`.koreatech.koin.domain.usecase.chat.GetLostItemChatRoomUseCase
import `in`.koreatech.koin.domain.usecase.chat.SubscribeChatRoomEventsUseCase
import `in`.koreatech.koin.domain.usecase.recruitment.chat.CreateOrGetRecruitmentDirectChatRoomUseCase
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
class ChatViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val getLostItemRoom: GetLostItemChatRoomUseCase,
    private val getRooms: GetChatRoomsUseCase,
    private val createDirectRoom: CreateOrGetRecruitmentDirectChatRoomUseCase,
    private val getChatRoomInfo: GetChatRoomInfoUseCase,
    private val blockUserUseCase: ChatBlockUserUseCase,
    private val messageSession: ChatMessageSession,
    private val sharedConnection: SharedChatConnection,
    private val getConnectionState: GetChatConnectionStateUseCase,
    private val subscribeRoom: SubscribeChatRoomEventsUseCase
) : ViewModel(), ContainerHost<ChatState, ChatSideEffect> {
    private val domainType = ChatDomain.valueOf(checkNotNull(savedStateHandle[EXTRA_CHAT_DOMAIN_TYPE])).toChatDomainType()
    private val contextId: Int = checkNotNull(savedStateHandle[EXTRA_CHAT_CONTEXT_ID])
    private val applicationId: Int? = savedStateHandle[EXTRA_APPLICATION_ID]
    private val requestedRoomId: Int? = savedStateHandle[EXTRA_CHAT_ROOM_ID]
    private val loadedRoomId = CompletableDeferred<String>()
    private var receivingMessagesJob: Job? = null

    override val container = container<ChatState, ChatSideEffect>(ChatState()) {
        observeMessages()
        loadRoom()
    }

    private fun observeMessages() = intent {
        repeatOnSubscription {
            launch { messageSession.observeCurrentUser() }
            messageSession.state.collect { messageState ->
                reduce { state.copy(messages = messageState.messages, uploadingImages = messageState.uploadingImages) }
            }
        }
    }

    private fun loadRoom() = intent {
        val roomResult = getRoom()
        roomResult.onSuccess { room ->
            messageSession.bind(room)
            reduce {
                state.copy(
                    roomId = room.roomId,
                    legacyRoomId = room.chatRoomId,
                    title = if (domainType == ChatDomainType.LOST_ITEM) room.roomName.replace("\n", " ") else room.counterpartNickname.orEmpty(),
                    profileImage = room.counterpartProfileImage,
                    canSend = room.canSend,
                    canBlockUser = domainType == ChatDomainType.LOST_ITEM,
                    supportsReadReceipts = room.supportsReadReceipts,
                    isLoading = false
                )
            }
            loadedRoomId.complete(room.roomId)
        }.onFailure { error ->
            reduce { state.copy(isLoading = false) }
            when (error) {
                is KoinChatException.BlockedException -> postSideEffect(ChatSideEffect.BlockedByUser)
                is KoinRecruitmentChatException.DirectChatConflictException -> postSideEffect(ChatSideEffect.DirectChatUnavailable)
                else -> postSideEffect(ChatSideEffect.FailedToCreateRoom)
            }
        }
    }

    private suspend fun getRoom(): Result<ChatRoomInfo> {
        if (domainType == ChatDomainType.LOST_ITEM) return getLostItemRoom(contextId, requestedRoomId)
        return suspendRunCatching {
            val resolvedRoomId = if (applicationId != null) {
                createDirectRoom(contextId, applicationId).getOrThrow().chatRoomId
            } else {
                requestedRoomId
            }
            val room = getRooms(domainType).getOrThrow().singleOrNull {
                it.domainType == domainType && it.contextId == contextId &&
                    (resolvedRoomId == null || it.chatRoomId == resolvedRoomId)
            } ?: throw KoinChatException.RoomNotFoundException()
            getChatRoomInfo(room.roomId).getOrThrow()
        }
    }

    private fun refreshRoomInfo(roomId: String) = intent {
        getChatRoomInfo(roomId).onSuccess { room ->
            messageSession.bind(room)
            reduce {
                state.copy(
                    legacyRoomId = room.chatRoomId,
                    title = if (domainType == ChatDomainType.LOST_ITEM) room.roomName.replace("\n", " ") else room.counterpartNickname.orEmpty(),
                    profileImage = room.counterpartProfileImage,
                    canSend = room.canSend,
                    supportsReadReceipts = room.supportsReadReceipts
                )
            }
        }.onFailure { Timber.e(it) }
    }

    @OptIn(OrbitExperimental::class)
    private suspend fun refreshMessages(roomId: String) = subIntent {
        refreshRoomInfo(roomId)
        messageSession.loadHistory().onFailure {
            if (!messageSession.hasHistoryCursor) postSideEffect(ChatSideEffect.FailedToLoadMessages)
        }
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
                            postSideEffect(ChatSideEffect.FailedToConnect)
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
                    postSideEffect(ChatSideEffect.FailedToConnect)
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
        if (state.roomId == null || state.chatInputValue.isBlank() || !state.canSend) return@intent
        messageSession.send(state.chatInputValue.trim()).onSuccess {
            reduce { state.copy(chatInputValue = "") }
        }.onFailure {
            postSideEffect(ChatSideEffect.FailedToSendMessage)
        }
    }

    fun uploadImage(fileSize: Long, fileType: String, fileName: String, imageUri: Uri) = intent {
        if (state.roomId == null || !state.canSend) return@intent
        messageSession.upload(fileSize, fileType, fileName, imageUri).onFailure { error ->
            postSideEffect(if (error is ChatImageUploadException) ChatSideEffect.FailedToUploadImage else ChatSideEffect.FailedToSendMessage)
        }
    }

    fun blockUser() = intent {
        if (!state.canBlockUser) return@intent
        val roomId = state.legacyRoomId ?: return@intent
        blockUserUseCase(contextId, roomId).onSuccess { postSideEffect(ChatSideEffect.BlockUserSuccess) }
            .onFailure { postSideEffect(ChatSideEffect.BlockUserFailed) }
    }

    fun updateMenu(showMenu: Boolean? = null, showBlockDialog: Boolean? = null) = intent {
        reduce {
            state.copy(
                showMenu = showMenu ?: state.showMenu,
                showBlockDialog = showBlockDialog ?: state.showBlockDialog
            )
        }
    }

    private companion object {
        const val RECONNECT_DELAY_MS = 1000L
    }
}
