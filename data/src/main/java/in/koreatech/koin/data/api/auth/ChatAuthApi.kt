package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAuthApi {
    @POST("chatroom/lost-item/{articleId}")
    suspend fun getChatRoomFromArticleId(@Path("articleId") articleId: Int): ChatRoomResponse
}