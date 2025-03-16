package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UploadFileUseCase @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        preSignedUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: String
    ): Result<Unit> {
        return withContext(coroutineDispatcher) {
            preSignedUrlRepository.uploadFile(preSignedUrl, imageUri, mediaType, mediaSize)
        }
    }
}
