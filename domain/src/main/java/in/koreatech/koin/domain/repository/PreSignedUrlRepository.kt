package `in`.koreatech.koin.domain.repository

import java.io.InputStream


interface PreSignedUrlRepository {
    suspend fun putPreSignedUrl(
        url: String,
        inputStream: InputStream,
        mediaType: String,
        mediaSize: Long
    ): Result<Unit>

    suspend fun uploadFile(
        url: String,
        bitmap: ByteArray,
        mediaType: String,
        mediaSize: Long
    ): Result<Unit>
}