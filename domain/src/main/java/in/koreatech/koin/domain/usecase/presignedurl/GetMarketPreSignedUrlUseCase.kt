package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import javax.inject.Inject

class GetMarketPreSignedUrlUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository
) {
    suspend operator fun invoke(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>> {
        return uploadUrlRepository.getUploadMarketUrlResult(contentLength, contentType, fileName)
    }
}