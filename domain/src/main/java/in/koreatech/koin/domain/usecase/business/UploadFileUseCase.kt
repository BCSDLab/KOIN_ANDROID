package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        url: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: String
    ): Result<Unit> {
        return withContext(coroutineDispatcher){
            preSignedUrlRepository.uploadFile(url,imageUri, mediaType, mediaSize)
        }
    }
}