package `in`.koreatech.koin.domain.usecase.presignedurl

import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val preSignedUrlRepository: PreSignedUrlRepository,
    private val uploadUrlRepository: UploadUrlRepository
) {
    suspend operator fun invoke(
        domain: PreSignedUrlDomain,
        contentLength: Long,
        contentType: String,
        fileName: String,
        imageUri: String
    ): Result<String> = runCatching {
        val preSignedUrl = uploadUrlRepository.getUploadUrlV2(
            domain = domain.domain,
            contentLength = contentLength,
            contentType = contentType,
            fileName = fileName
        ).getOrThrow()

        preSignedUrlRepository.uploadFile(
            url = preSignedUrl.preSignedUrl,
            imageUri = imageUri,
            mediaType = contentType,
            mediaSize = contentLength
        ).getOrThrow()

        preSignedUrl.fileUrl
    }
}
