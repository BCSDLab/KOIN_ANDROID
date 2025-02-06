package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ChatApi
import `in`.koreatech.koin.data.api.auth.ChatAuthApi
import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import `in`.koreatech.koin.data.stomp.KoinStomp
import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val chatApi: ChatApi,
    private val chatAuthApi: ChatAuthApi,
    private val koinStomp: KoinStomp
) {
    suspend fun connectWS() {
        koinStomp.connect()
    }

    suspend fun disconnectWS() {
        koinStomp.disconnect()
    }

    suspend fun getChatRoomFromArticleId(articleId: Int): ChatRoomResponse {
        return chatAuthApi.getChatRoomFromArticleId(articleId)
    }

    suspend fun getChatMessages(articleId: Int, chatRoomId: Int): List<ChatMessageResponse> {
        return chatAuthApi.getChatMessages(articleId, chatRoomId)
    }

    suspend fun subscribeChatRoom(articleId: Int, chatRoomId: Int): Flow<ChatMessageResponse> {
        return koinStomp.subscribe(
            "/topic/chat/$articleId/$chatRoomId",
            ChatMessageResponse.serializer()
        )
    }

    suspend fun sendMessage(articleId: Int, chatRoomId: Int, message: ChatMessageRequest) {
        koinStomp.convertAndSend("/app/chat/$articleId/$chatRoomId", message)
    }

    suspend fun blockUser(articleId: Int, chatRoomId: Int): Result<Unit> {
        val response = chatAuthApi.blockUser(articleId, chatRoomId)
        if (response.isSuccessful) {
            return Result.success(Unit)
        } else {
            return Result.failure(Exception(response.message))
        }
    }
}
