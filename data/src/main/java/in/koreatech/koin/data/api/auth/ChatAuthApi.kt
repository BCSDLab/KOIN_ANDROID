package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.chat.ChatReadRequest
import `in`.koreatech.koin.data.request.chat.SendChatMessageRequest
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomInfoResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatAuthApi {
    @GET("chatroom/lost-item")
    @Deprecated("")
    suspend fun getChatRoomList(): List<ChatListItemResponse>

    @POST("chatroom/lost-item/{article_id}")
    @Deprecated("")
    suspend fun getChatRoomFromArticleId(
        @Path("article_id") articleId: Int
    ): ChatRoomResponse

    @GET("chatroom/lost-item/{article_id}/{chat_room_id}")
    @Deprecated("")
    suspend fun getChatRoom(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): ChatRoomResponse

    @GET("chatroom/lost-item/{article_id}/{chat_room_id}/messages")
    @Deprecated("")
    suspend fun getChatMessages(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): List<ChatMessageResponse>

    @POST("chatroom/lost-item/{article_id}/{chat_room_id}/block")
    @Deprecated("")
    suspend fun blockUser(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): Response<Unit>

    @GET("chatrooms")
    suspend fun getChatRooms(@Query("domain_type") domainType: String?): List<ChatRoomInfoResponse>

    @GET("chatrooms/{roomId}")
    suspend fun getChatRoomInfo(@Path("roomId") roomId: String): ChatRoomInfoResponse

    @GET("chatrooms/{roomId}/messages")
    suspend fun getChatRoomMessages(
        @Path("roomId") roomId: String,
        @Query("after_message_id") afterMessageId: String?,
        @Query("before_message_id") beforeMessageId: String?,
        @Query("limit") limit: Int
    ): List<ChatRoomMessageResponse>

    @POST("chatrooms/{roomId}/messages")
    suspend fun sendChatMessage(
        @Path("roomId") roomId: String,
        @Body message: SendChatMessageRequest
    ): ChatRoomMessageResponse

    @POST("chatrooms/{roomId}/read")
    suspend fun markChatRead(@Path("roomId") roomId: String, @Body request: ChatReadRequest): Unit
}
