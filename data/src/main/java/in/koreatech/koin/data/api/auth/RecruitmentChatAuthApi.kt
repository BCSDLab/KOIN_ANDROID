package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.recruitment.chat.CreateChatMessageRequest
import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatMessageResponse
import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatRoomResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecruitmentChatAuthApi {
    @GET("/chatroom/team-recruitment/{recruitmentId}/{chatRoomId}")
    suspend fun getChatRoom(
        @Path("recruitmentId") recruitmentId: Int,
        @Path("chatRoomId") chatRoomId: Int
    ): RecruitmentChatRoomResponse

    @POST("/chatroom/team-recruitment/{recruitmentId}/applications/{applicationId}/direct")
    suspend fun createOrGetDirectChatRoom(
        @Path("recruitmentId") recruitmentId: Int,
        @Path("applicationId") applicationId: Int
    ): RecruitmentChatRoomResponse

    @GET("/chatroom/team-recruitment/{recruitmentId}/{chatRoomId}/messages")
    suspend fun getMessages(
        @Path("recruitmentId") recruitmentId: Int,
        @Path("chatRoomId") chatRoomId: Int,
        @Query("afterMessageId") afterMessageId: Int?,
        @Query("beforeMessageId") beforeMessageId: Int?,
        @Query("limit") limit: Int
    ): List<RecruitmentChatMessageResponse>

    @POST("/chatroom/team-recruitment/{recruitmentId}/{chatRoomId}/messages")
    suspend fun sendMessage(
        @Path("recruitmentId") recruitmentId: Int,
        @Path("chatRoomId") chatRoomId: Int,
        @Body request: CreateChatMessageRequest
    ): RecruitmentChatMessageResponse
}
