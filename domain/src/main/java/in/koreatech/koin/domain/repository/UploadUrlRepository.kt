package `in`.koreatech.koin.domain.repository

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
}