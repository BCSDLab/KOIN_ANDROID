package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetMarketPreSignedUrlUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>> {
        return withContext(coroutineDispatcher) {
            uploadUrlRepository.getUploadMarketUrlResult(contentLength, contentType, fileName)
        }
    }
}
