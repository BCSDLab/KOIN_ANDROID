package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.model.upload.PreSignedUrl
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClubPreSignedUrlUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<PreSignedUrl> {
        return withContext(coroutineDispatcher) {
            uploadUrlRepository.getUploadClubUrlResult(contentLength, contentType, fileName)
        }
    }
}
