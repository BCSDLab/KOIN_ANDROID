package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.upload.PreSignedUrl

interface UploadUrlRepository {
    suspend fun getUploadMarketUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>>

    suspend fun getUploadUrlV2(
        domain: String,
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<PreSignedUrl>
}
