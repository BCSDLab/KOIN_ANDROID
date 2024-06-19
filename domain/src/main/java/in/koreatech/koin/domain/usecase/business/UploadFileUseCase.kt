package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository
) {
    suspend operator fun invoke(
        url: String,
        bitmap: String,
        mediaType: String,
        mediaSize: Long
    ): Result<Unit> {
        return preSignedUrlRepository.uploadFile(url, bitmap, mediaType, mediaSize)
    }
}