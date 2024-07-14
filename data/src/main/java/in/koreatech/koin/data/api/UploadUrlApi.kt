package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.response.upload.UploadUrlResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadUrlApi {
    @POST(URLConstant.UPLOAD.OWNERURL)
    suspend fun postUploadUrl(@Body uploadUrlRequest: UploadUrlRequest): UploadUrlResponse

    @POST(URLConstant.UPLOAD.MARKETURL)
    suspend fun postUploadMarketUrl(@Body uploadUrlRequest: UploadUrlRequest): UploadUrlResponse
}