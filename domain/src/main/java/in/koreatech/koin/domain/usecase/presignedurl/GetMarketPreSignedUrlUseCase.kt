package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMarketPreSignedUrlUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>> {
        return withContext(coroutineDispatcher){
            uploadUrlRepository.getUploadMarketUrlResult(contentLength, contentType, fileName)
        }
    }
}