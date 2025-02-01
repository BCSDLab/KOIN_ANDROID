package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.model.chat.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connectWS()
    suspend fun disconnectWS()
    suspend fun getChatRoomFromArticleId(articleId: Int): Flow<ChatRoom>
    suspend fun getChatMessages(articleId: Int, chatRoomId: Int): Flow<List<ChatMessage>>
    suspend fun subscribeChatRoom(articleId: Int, chatRoomId: Int): Flow<ChatMessage>
    suspend fun sendMessage(articleId: Int, chatRoomId: Int, message: ChatMessage)
}