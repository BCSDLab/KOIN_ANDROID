package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import javax.inject.Inject

class AttachStoreFileUseCase @Inject constructor(
    private val uploadUrlRepository: UploadUrlRepository
) {
    suspend operator fun invoke(
        content_length: Long,
        content_type: String,
        file_name: String
    ): Result<Unit> {
        return uploadUrlRepository.getUploadUrlResult(content_length, content_type, file_name)
    }
}