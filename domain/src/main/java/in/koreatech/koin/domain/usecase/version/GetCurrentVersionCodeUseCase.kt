package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.repository.VersionRepository
import javax.inject.Inject

class GetCurrentVersionCodeUseCase @Inject constructor(
    private val versionRepository: VersionRepository
) {
    suspend operator fun invoke(): Result<Int> =
        runCatching {
            versionRepository.getCurrentVersionCode()
                ?: return Result.failure(NullPointerException("Failed to load client application version"))
        }
}
