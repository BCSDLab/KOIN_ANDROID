package `in`.koreatech.koin.domain.repository

interface UploadUrlRepository {
    suspend fun getUploadUrlResult(
        content_length: Long,
        content_type: String,
        file_name: String
    ): Result<Unit>
}