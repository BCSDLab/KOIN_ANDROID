package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApi {
    @GET("chatroom/lost-item/{article_id}/{chat_room_id}/messages")
    suspend fun getChatMessages(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): List<ChatMessageResponse>
}