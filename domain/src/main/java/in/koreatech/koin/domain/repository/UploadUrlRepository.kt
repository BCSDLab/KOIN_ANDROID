package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.upload.PreSignedUrl

interface UploadUrlRepository {
    suspend fun getUploadUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>>

    suspend fun getUploadMarketUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>>

    suspend fun getUploadLostAndFoundUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>>

    suspend fun getUploadClubUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<PreSignedUrl>
}
