package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.chat.ChatListItemResponse
import `in`.koreatech.koin.data.response.chat.ChatMessageResponse
import `in`.koreatech.koin.data.response.chat.ChatRoomResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAuthApi {
    @GET(URLConstant.CHAT.CHATROOM)
    suspend fun getChatRoomList(): List<ChatListItemResponse>

    @POST(URLConstant.CHAT.CHATROOM_ARTICLEID)
    suspend fun getChatRoomFromArticleId(
        @Path("articleId") articleId: Int
    ): ChatRoomResponse

    @GET(URLConstant.CHAT.CHATROOM_ARTICLEID_ROOMID)
    suspend fun getChatRoom(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): ChatRoomResponse

    @GET(URLConstant.CHAT.MESSAGES)
    suspend fun getChatMessages(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): List<ChatMessageResponse>

    @POST(URLConstant.CHAT.BLOCK)
    suspend fun blockUser(
        @Path("article_id") articleId: Int,
        @Path("chat_room_id") chatRoomId: Int
    ): Response<Unit>
}
