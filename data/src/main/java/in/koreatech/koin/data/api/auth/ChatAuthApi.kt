package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import retrofit2.http.POST

interface ChatAuthApi {
    @POST("chatroom/lost-item/{articleId}")
    suspend fun getChatRoomFromArticleId(articleId: Int): ChatRoomResponse
}