package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.model.chat.ChatRoom
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWS()

    suspend fun disconnectWS()

    suspend fun getChatRoomList(): Flow<List<ChatListItem>>

    suspend fun getChatRoomFromArticleId(articleId: Int): Result<ChatRoom>

    suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Result<ChatRoom>

    suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): Flow<List<ChatMessage>>

    fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage>

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
