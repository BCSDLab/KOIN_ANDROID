package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.ChatApi
import `in`.koreatech.koin.data.api.auth.ChatAuthApi
import `in`.koreatech.koin.data.request.chat.ChatMessageRequest
import `in`.koreatech.koin.data.request.chat.ChatReadRequest
import `in`.koreatech.koin.data.request.chat.SendChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomEventResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomInfoResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import `in`.koreatech.koin.data.stomp.KoinStomp
import `in`.koreatech.koin.data.stomp.KoinStompConnectionState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

// Keep deprecated APIs alongside common APIs until their callers migrate.
@Suppress("TooManyFunctions")
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

    @Deprecated("")
    suspend fun getChatRoomList(): List<ChatListItemResponse> {
        return chatAuthApi.getChatRoomList()
    }

    @Deprecated("")
    suspend fun getChatRoomFromArticleId(articleId: Int): ChatRoomResponse {
        return chatAuthApi.getChatRoomFromArticleId(articleId)
    }

    @Deprecated("")
    suspend fun getChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): ChatRoomResponse {
        return chatAuthApi.getChatRoom(articleId, chatRoomId)
    }

    @Deprecated("")
    suspend fun getChatMessages(
        articleId: Int,
        chatRoomId: Int
    ): List<ChatMessageResponse> {
        return chatAuthApi.getChatMessages(articleId, chatRoomId)
    }

    @Deprecated("")
    fun subscribeChatRoom(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessageResponse> = flow {
        koinStomp.subscribe("/topic/chat/$articleId/$chatRoomId").collect { payload ->
            emit(json.decodeFromString(ChatMessageResponse.serializer(), payload))
        }
    }

    @Deprecated("")
    fun subscribeChatList(
        userId: Int
    ): Flow<List<ChatListItemResponse>> = flow {
        koinStomp.subscribe("/topic/chatroom/list/$userId").collect { payload ->
            emit(json.decodeFromString(ListSerializer(ChatListItemResponse.serializer()), payload))
        }
    }

    @Deprecated("")
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

    @Deprecated("")
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

    suspend fun getChatRooms(domainType: String?): List<ChatRoomInfoResponse> =
        chatAuthApi.getChatRooms(domainType)

    suspend fun getChatRoomInfo(roomId: String): ChatRoomInfoResponse = chatAuthApi.getChatRoomInfo(roomId)

    suspend fun getChatRoomMessages(
        roomId: String,
        afterMessageId: String?,
        beforeMessageId: String?,
        limit: Int
    ): List<ChatRoomMessageResponse> = chatAuthApi.getChatRoomMessages(roomId, afterMessageId, beforeMessageId, limit)

    suspend fun subscribeChatRoomEvents(roomId: String): Flow<ChatRoomEventResponse> {
        val frames = koinStomp.subscribe("/topic/chatrooms/$roomId")
        return frames.map { json.decodeFromString(ChatRoomEventResponse.serializer(), it) }
    }

    suspend fun sendChatMessage(roomId: String, message: SendChatMessageRequest): ChatRoomMessageResponse =
        chatAuthApi.sendChatMessage(roomId, message)

    suspend fun markChatRead(roomId: String, request: ChatReadRequest): Unit =
        chatAuthApi.markChatRead(roomId, request)
}
