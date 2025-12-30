package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ChatApi
import `in`.koreatech.koin.data.api.auth.ChatAuthApi
import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import `in`.koreatech.koin.data.stomp.KoinStomp
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

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

    suspend fun getChatRoomList(): List<ChatListItemResponse> {
        return chatAuthApi.getChatRoomList()
    }

    suspend fun getChatRoomFromArticleId(articleId: Int): ChatRoomResponse {
        return chatAuthApi.getChatRoomFromArticleId(articleId)
    }

    suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): ChatRoomResponse {
        return chatAuthApi.getChatRoom(articleId, chatRoomId)
    }

    suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): List<ChatMessageResponse> {
        return chatAuthApi.getChatMessages(articleId, chatRoomId)
    }

    fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessageResponse> {
        return koinStomp.subscribe(
            "/topic/chat/$articleId/$chatRoomId",
            ChatMessageResponse.serializer()
        )
    }

    suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessageRequest
    ): Result<Unit> {
        return runCatching {
            koinStomp.convertAndSend("/app/chat/$articleId/$chatRoomId", message)
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }

    suspend fun blockUser(
        articleId: Int,
        chatRoomId: Int
    ): Result<Unit> {
        val response = chatAuthApi.blockUser(articleId, chatRoomId)
        if (response.isSuccessful) {
            return Result.success(Unit)
        } else {
            return Result.failure(Exception(response.message()))
        }
    }
}
