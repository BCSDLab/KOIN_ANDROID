package `in`.koreatech.koin.domain.repository

import java.io.InputStream


interface PreSignedUrlRepository {
    suspend fun putPreSignedUrl(
        url: String,
        inputStream: InputStream,
        mediaType: String,
        mediaSize: Long
    ): Result<Unit>
}