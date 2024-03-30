package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import javax.inject.Inject

class AttachStoreFileUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository
) {
    suspend operator fun invoke(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>> {
        return uploadUrlRepository.getUploadUrlResult(contentLength, contentType, fileName)
    }
}