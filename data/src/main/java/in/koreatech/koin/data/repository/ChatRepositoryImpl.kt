package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toChatMessageRequest
import `in`.koreatech.koin.data.mapper.toChatRoomInfo
import `in`.koreatech.koin.data.mapper.toChatRoomMessage
import `in`.koreatech.koin.data.request.chat.ChatReadRequest
import `in`.koreatech.koin.data.request.chat.SendChatMessageRequest
import `in`.koreatech.koin.data.response.chat.toChatListItem
import `in`.koreatech.koin.data.source.remote.ChatRemoteDataSource
import `in`.koreatech.koin.data.stomp.KoinStompConnectionState
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.chat.KoinChatException
import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

    override suspend fun getChatRoomList(): Flow<List<ChatListItem>> {
        return flow {
            emit(chatRemoteDataSource.getChatRoomList().map { it.toChatListItem() })
        }
    }

    override suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatRoomFromArticleId(articleId).toChatRoom()
        }.mapHttpFailure {
            on(403) throws KoinChatException.BlockedException()
        }
    }

    override suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatRoom(articleId, chatRoomId).toChatRoom()
        }
    }

    override suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>> {
        return suspendRunCatching {
            chatRemoteDataSource.getChatMessages(articleId, chatRoomId).map { it.toChatMessage() }
        }
    }

    override fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage> {
        return chatRemoteDataSource.subscribeChatRoom(articleId, chatRoomId).map { it.toChatMessage() }
    }

    override fun subscribeChatList(userId: Int): Flow<List<ChatListItem>> {
        return chatRemoteDataSource.subscribeChatList(userId).map { it.map { it.toChatListItem() } }
    }

    override suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit> {
        return chatRemoteDataSource.sendMessage(articleId, chatRoomId, message.toChatMessageRequest())
    }

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

    override suspend fun sendChatMessage(roomId: String, content: String, isImage: Boolean): Result<ChatRoomMessage> = suspendRunCatching {
        chatRemoteDataSource.sendChatMessage(roomId, SendChatMessageRequest(content, isImage)).toChatRoomMessage()
    }.mapChatFailure()

    override suspend fun markChatRead(roomId: String, messageId: String): Result<Unit> = suspendRunCatching {
        chatRemoteDataSource.markChatRead(roomId, ChatReadRequest(messageId))
    }.mapChatFailure()

    override suspend fun createLostItemChatRoom(articleId: Int): Result<ChatRoomInfo> = suspendRunCatching {
        val legacyRoom = chatRemoteDataSource.getChatRoomFromArticleId(articleId)
        val room = chatRemoteDataSource.getChatRooms(ChatDomainType.LOST_ITEM.name)
            .single { it.contextId == articleId && it.chatRoomId == legacyRoom.chatRoomId }
        chatRemoteDataSource.getChatRoomInfo(room.roomId).toChatRoomInfo()
    }.mapChatFailure()

    private fun <T> Result<T>.mapChatFailure(): Result<T> = mapHttpFailure {
        on(401) throws KoinChatException.UnauthorizedException()
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
}
