package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.model.chat.ChatRoomEvent
import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /** Returns connection failures; coroutine cancellation is propagated. */
    suspend fun connectWS(): Result<Unit>

    /** Returns disconnection failures; coroutine cancellation is propagated. */
    suspend fun disconnectWS(): Result<Unit>

    fun observeConnectionState(): Flow<ChatConnectionState>

    @Deprecated("")
    suspend fun getChatRoomList(): Flow<List<ChatListItem>>

    @Deprecated("")
    suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom>

    @Deprecated("")
    suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom>

    @Deprecated("")
    suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>>

    @Deprecated("")
    fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage>

    @Deprecated("")
    fun subscribeChatList(
        userId: Int
    ): Flow<List<ChatListItem>>

    @Deprecated("")
    suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit>

    @Deprecated("")
    suspend fun blockUser(
        articleId: Int,
        chatRoomId: Int
    ): Result<Unit>

    suspend fun getChatRooms(domainType: ChatDomainType? = null): Result<List<ChatRoomInfo>>

    suspend fun getChatRoomInfo(roomId: String): Result<ChatRoomInfo>

    suspend fun getChatRoomMessages(
        roomId: String,
        afterMessageId: String? = null,
        beforeMessageId: String? = null,
        limit: Int = 100
    ): Result<List<ChatRoomMessage>>

    /** Registers this subscription before returning; cancellation releases only its own subscription. */
    suspend fun subscribeChatRoomEvents(roomId: String): Flow<ChatRoomEvent>

    suspend fun sendChatMessage(roomId: String, content: String, isImage: Boolean): Result<ChatRoomMessage>

    suspend fun markChatRead(roomId: String, messageId: String): Result<Unit>

    suspend fun createLostItemChatRoom(articleId: Int): Result<ChatRoomInfo>
}
