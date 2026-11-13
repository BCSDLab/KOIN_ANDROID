package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /** Returns connection failures; coroutine cancellation is propagated. */
    suspend fun connectWS(): Result<Unit>

    /** Returns disconnection failures; coroutine cancellation is propagated. */
    suspend fun disconnectWS(): Result<Unit>

    fun observeConnectionState(): Flow<ChatConnectionState>

    suspend fun getChatRoomList(): Flow<List<ChatListItem>>

    suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom>

    suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom>

    suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>>

    fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage>

    fun subscribeChatList(
        userId: Int
    ): Flow<List<ChatListItem>>

    suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ): Result<Unit>

    suspend fun blockUser(
        articleId: Int,
        chatRoomId: Int
    ): Result<Unit>
}
