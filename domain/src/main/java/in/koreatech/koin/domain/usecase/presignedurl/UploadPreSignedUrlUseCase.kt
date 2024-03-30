package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import java.io.InputStream
import javax.inject.Inject

class UploadPreSignedUrlUseCase @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository
) {
    suspend operator fun invoke(
        url: String,
        inputStream: InputStream,
        mediaType: String,
        mediaSize: Long
    ): Result<Unit> {
        return preSignedUrlRepository.putPreSignedUrl(url, inputStream, mediaType, mediaSize)
    }
}