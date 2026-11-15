package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ChatApi
import `in`.koreatech.koin.data.api.auth.ChatAuthApi
import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import `in`.koreatech.koin.data.stomp.KoinStomp
import `in`.koreatech.koin.data.stomp.KoinStompConnectionState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ChatRemoteDataSource @Inject constructor(
    private val chatApi: ChatApi,
    private val chatAuthApi: ChatAuthApi,
    private val koinStomp: KoinStomp,
    private val json: Json
) {
    val connectionState: Flow<KoinStompConnectionState> = koinStomp.connectionState

    suspend fun connectWS(): Result<Unit> {
        return koinStomp.connect()
    }

    suspend fun disconnectWS(): Result<Unit> {
        return koinStomp.disconnect()
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
    ): Flow<ChatMessageResponse> = flow {
        koinStomp.subscribe("/topic/chat/$articleId/$chatRoomId").collect { payload ->
            emit(json.decodeFromString(ChatMessageResponse.serializer(), payload))
        }
    }

    fun subscribeChatList(
        userId: Int
    ): Flow<List<ChatListItemResponse>> = flow {
        koinStomp.subscribe("/topic/chatroom/list/$userId").collect { payload ->
            emit(json.decodeFromString(ListSerializer(ChatListItemResponse.serializer()), payload))
        }
    }

    suspend fun sendMessage(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessageRequest
    ): Result<Unit> {
        return koinStomp.sendMessage(
            destination = "/app/chat/$articleId/$chatRoomId",
            message = message,
            serializer = ChatMessageRequest.serializer()
        )
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
