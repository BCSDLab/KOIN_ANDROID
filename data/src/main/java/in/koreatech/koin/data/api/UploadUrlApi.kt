package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.response.upload.UploadUrlResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface UploadUrlApi {
    @POST("/market/upload/url")
    suspend fun postUploadMarketUrl(
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse

    @POST("/{domain}/upload/url")
    suspend fun postUploadUrlV2(
        @Path("domain") domain: String,
        @Body uploadUrlRequest: UploadUrlRequest
    ): UploadUrlResponse
}
