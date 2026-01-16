package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.response.upload.UploadUrlResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadUrlApi {
    @POST("/owners/upload/url")
    suspend fun postUploadUrl(
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse

    @POST("/market/upload/url")
    suspend fun postUploadMarketUrl(
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse

    @POST("/lost_items/upload/url")
    suspend fun postUploadLostAndFoundUrl(
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse

    @POST("/club/upload/url")
    suspend fun postUploadClubUrl(
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse
}
