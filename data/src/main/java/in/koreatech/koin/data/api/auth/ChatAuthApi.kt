package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAuthApi {
    @POST("chatroom/lost-item/{articleId}")
    suspend fun getChatRoomFromArticleId(@Path("articleId") articleId: Int): ChatRoomResponse

    @GET("chatroom/lost-item/{article_id}/{chat_room_id}/messages")
    suspend fun getChatMessages(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): List<ChatMessageResponse>
}