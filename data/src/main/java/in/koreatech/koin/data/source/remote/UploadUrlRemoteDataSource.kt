package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UploadUrlApi
import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.response.upload.UploadUrlResponse
import javax.inject.Inject

class UploadUrlRemoteDataSource @Inject constructor(
    private val uploadUrl: UploadUrlApi
) {
    suspend fun postUploadMarketUrl(uploadUrlRequest: UploadUrlRequest): UploadUrlResponse =
        uploadUrl.postUploadMarketUrl(
            uploadUrlRequest
        )

    suspend fun postUploadUrlV2(domain: String, uploadUrlRequest: UploadUrlRequest): UploadUrlResponse = uploadUrl.postUploadUrlV2(domain, uploadUrlRequest)
}
