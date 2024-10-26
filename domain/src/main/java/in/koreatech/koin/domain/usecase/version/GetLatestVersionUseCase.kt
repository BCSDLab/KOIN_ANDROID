package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.repository.VersionRepository
import javax.inject.Inject

class GetLatestVersionUseCase @Inject constructor(
    private val versionRepository: VersionRepository
) {
    suspend operator fun invoke(): Result<Pair<String, String>> = runCatching {
        val currentVersion = versionRepository.getCurrentVersionName()
            ?: return Result.failure(NullPointerException("Failed to load client application version"))

        val latestVersion = versionRepository.getLatestVersionName()
            ?: return Result.failure(NullPointerException("Failed to load latest application version"))

        currentVersion to latestVersion
    }
}