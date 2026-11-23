package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toChatListItem
import `in`.koreatech.koin.data.mapper.toChatMessage
import `in`.koreatech.koin.data.mapper.toChatMessageRequest
import `in`.koreatech.koin.data.mapper.toChatRoom
import `in`.koreatech.koin.data.mapper.toChatRoomEvent
import `in`.koreatech.koin.data.mapper.toChatRoomInfo
import `in`.koreatech.koin.data.mapper.toChatRoomMessage
import `in`.koreatech.koin.data.request.chat.ChatReadRequest
import `in`.koreatech.koin.data.request.chat.SendChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatRoomEventResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomInfoResponse
import `in`.koreatech.koin.data.source.remote.ChatRemoteDataSource
import `in`.koreatech.koin.data.stomp.KoinStompConnectionState
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.model.chat.ChatRoomEvent
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import timber.log.Timber

class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource
) : ChatRepository {
    override suspend fun connectWS(): Result<Unit> {
        return chatRemoteDataSource.connectWS()
    }

    override suspend fun disconnectWS(): Result<Unit> {
        return chatRemoteDataSource.disconnectWS()
    }

    override fun observeConnectionState(): Flow<ChatConnectionState> {
        return chatRemoteDataSource.connectionState.map { it.toChatConnectionState() }
    }

    @Deprecated("")
    override suspend fun getChatRoomList(): Flow<List<ChatListItem>> {
        return flow {
            emit(chatRemoteDataSource.getChatRoomList().map { it.toChatListItem() })
        }
    }

    @Deprecated("")
    override suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatRoomFromArticleId(articleId).toChatRoom()
        }.mapHttpFailure {
            on(403) throws KoinChatException.BlockedException()
        }
    }

    @Deprecated("")
    override suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatRoom(articleId, chatRoomId).toChatRoom()
        }
    }

    @Deprecated("")
    override suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatMessages(articleId, chatRoomId).map { it.toChatMessage() }
        }
    }

    @Deprecated("")
    override fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage> {
        return chatRemoteDataSource.subscribeChatRoom(articleId, chatRoomId).map { it.toChatMessage() }
    }

    @Deprecated("")
    override fun subscribeChatList(userId: Int): Flow<List<ChatListItem>> {
        return chatRemoteDataSource.subscribeChatList(userId).map { it.map { it.toChatListItem() } }
    }

    @Deprecated("")
    override suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit> {
        return chatRemoteDataSource.sendMessage(articleId, chatRoomId, message.toChatMessageRequest())
    }

    override fun getLostItemChatRoomList(): Flow<List<ChatRoomInfo>> = flow {
        emit(loadChatList())
    }

    override suspend fun getLostItemChatRoom(articleId: Int, chatRoomId: Int?): Result<ChatRoomInfo> = suspendRunCatching {
        val room = if (chatRoomId == null) {
            resolveLostItemRoomInfo(articleId)
        } else {
            val existing = findLostItemRoom(articleId, chatRoomId)
            chatRemoteDataSource.getChatRoomInfo(existing.roomId)
        }
        room.toChatRoomInfo()
    }.mapChatFailure()

    override suspend fun getLostItemChatMessages(articleId: Int, chatRoomId: Int): Result<List<ChatRoomMessage>> = suspendRunCatching {
        val room = findLostItemRoom(articleId, chatRoomId)
        val messages = ArrayDeque<ChatRoomMessage>()
        var beforeMessageId: String? = null
        do {
            val page = chatRemoteDataSource.getChatRoomMessages(
                roomId = room.roomId,
                afterMessageId = null,
                beforeMessageId = beforeMessageId,
                limit = MESSAGE_PAGE_SIZE
            )
            for (index in page.indices.reversed()) {
                messages.addFirst(page[index].toChatRoomMessage())
            }
            beforeMessageId = page.firstOrNull()?.messageId
        } while (page.size == MESSAGE_PAGE_SIZE)
        messages.toList()
    }.mapChatFailure()

    override fun subscribeLostItemChatRoom(articleId: Int, chatRoomId: Int): Flow<ChatRoomMessage> = flow {
        val room = suspendRunCatching { findLostItemRoom(articleId, chatRoomId) }.mapChatFailure().getOrThrow()
        chatRemoteDataSource.subscribeChatRoomEvents(room.roomId).collect { event ->
            if (event.type == ChatRoomEventResponse.Type.MESSAGE_CREATED) {
                emit(requireNotNull(event.message).toChatRoomMessage())
            }
        }
    }

    override fun subscribeLostItemChatList(): Flow<List<ChatRoomInfo>> {
        return flow {
            while (currentCoroutineContext().isActive) {
                val result = suspendRunCatching { loadChatList() }
                result.onFailure { Timber.e(it, "Failed to load lost item chat list") }
                result.getOrNull()?.let { emit(it) }
                delay(CHAT_LIST_REFRESH_INTERVAL_MS)
            }
        }.distinctUntilChanged()
    }

    override suspend fun sendLostItemChatMessage(
        articleId: Int,
        chatRoomId: Int,
        content: String,
        isImage: Boolean
    ): Result<ChatRoomMessage> = suspendRunCatching {
        val room = findLostItemRoom(articleId, chatRoomId)
        chatRemoteDataSource.sendChatMessage(room.roomId, SendChatMessageRequest(content, isImage)).toChatRoomMessage()
    }.mapChatFailure()

    @Deprecated("")
    override suspend fun blockUser(
        articleId: Int,
        chatRoomId: Int
    ): Result<Unit> {
        return chatRemoteDataSource.blockUser(articleId, chatRoomId)
    }

    override suspend fun getChatRooms(domainType: ChatDomainType?): Result<List<ChatRoomInfo>> = suspendRunCatching {
        chatRemoteDataSource.getChatRooms(domainType?.name).map { it.toChatRoomInfo() }
    }.mapChatFailure()

    override suspend fun getChatRoomInfo(roomId: String): Result<ChatRoomInfo> = suspendRunCatching {
        chatRemoteDataSource.getChatRoomInfo(roomId).toChatRoomInfo()
    }.mapChatFailure()

    override suspend fun getChatRoomMessages(
        roomId: String,
        afterMessageId: String?,
        beforeMessageId: String?,
        limit: Int
    ): Result<List<ChatRoomMessage>> = suspendRunCatching {
        chatRemoteDataSource.getChatRoomMessages(roomId, afterMessageId, beforeMessageId, limit).map { it.toChatRoomMessage() }
    }.mapChatFailure()

    override suspend fun subscribeChatRoomEvents(roomId: String): Flow<ChatRoomEvent> =
        chatRemoteDataSource.subscribeChatRoomEvents(roomId).map { it.toChatRoomEvent() }

    override suspend fun sendChatMessage(roomId: String, content: String, isImage: Boolean): Result<ChatRoomMessage> = suspendRunCatching {
        chatRemoteDataSource.sendChatMessage(roomId, SendChatMessageRequest(content, isImage)).toChatRoomMessage()
    }.mapChatFailure()

    override suspend fun markChatRead(roomId: String, messageId: String): Result<Unit> = suspendRunCatching {
        chatRemoteDataSource.markChatRead(roomId, ChatReadRequest(messageId))
    }.mapChatFailure()

    override suspend fun updateChatPresence(roomId: String): Result<Unit> = suspendRunCatching {
        chatRemoteDataSource.updateChatPresence(roomId)
    }.mapChatFailure()

    override suspend fun leaveChatPresence(roomId: String): Result<Unit> = suspendRunCatching {
        chatRemoteDataSource.leaveChatPresence(roomId)
    }.mapChatFailure()

    override suspend fun createLostItemChatRoom(articleId: Int): Result<ChatRoomInfo> = suspendRunCatching {
        resolveLostItemRoomInfo(articleId).toChatRoomInfo()
    }.mapChatFailure()

    private suspend fun loadChatList(): List<ChatRoomInfo> = suspendRunCatching {
        chatRemoteDataSource.getChatRooms(ChatDomainType.LOST_ITEM.name)
            .filter { it.domainType == ChatDomainType.LOST_ITEM.name }
            .map { it.toChatRoomInfo() }
    }.mapChatFailure().getOrThrow()

    private suspend fun findLostItemRoom(articleId: Int, chatRoomId: Int): ChatRoomInfoResponse =
        chatRemoteDataSource.getChatRooms(ChatDomainType.LOST_ITEM.name)
            .singleOrNull { it.domainType == ChatDomainType.LOST_ITEM.name && it.contextId == articleId && it.chatRoomId == chatRoomId }
            ?: throw KoinChatException.RoomNotFoundException()

    private suspend fun resolveLostItemRoomInfo(articleId: Int): ChatRoomInfoResponse {
        val existing = chatRemoteDataSource.getChatRooms(ChatDomainType.LOST_ITEM.name)
            .filter { it.domainType == ChatDomainType.LOST_ITEM.name && it.contextId == articleId }
        val roomId = when (existing.size) {
            0 -> {
                val created = chatRemoteDataSource.getChatRoomFromArticleId(articleId)
                findLostItemRoom(articleId, created.chatRoomId).roomId
            }
            1 -> existing.single().roomId
            else -> throw KoinChatException.InvalidRequestException()
        }
        return chatRemoteDataSource.getChatRoomInfo(roomId)
    }

    private fun <T> Result<T>.mapChatFailure(): Result<T> = mapHttpFailure {
        on(400) throws KoinChatException.InvalidRequestException()
        on(401) throws KoinChatException.UnauthorizedException()
        on(401, "WITHDRAWN_USER") throws KoinChatException.WithdrawnUserException()
        on(403) throws KoinChatException.AccessDeniedException()
        on(404) throws KoinChatException.RoomNotFoundException()
        on(403, "FORBIDDEN_BLOCKED_USER") throws KoinChatException.BlockedException()
    }

    private fun KoinStompConnectionState.toChatConnectionState(): ChatConnectionState {
        return when (this) {
            is KoinStompConnectionState.Connecting -> ChatConnectionState.CONNECTING
            is KoinStompConnectionState.Connected -> ChatConnectionState.CONNECTED
            is KoinStompConnectionState.Reconnecting -> ChatConnectionState.RECONNECTING
            is KoinStompConnectionState.Disconnected -> ChatConnectionState.DISCONNECTED
        }
    }

    private companion object {
        const val MESSAGE_PAGE_SIZE = 100
        const val CHAT_LIST_REFRESH_INTERVAL_MS = 5000L
    }
}
