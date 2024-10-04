package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.repository.VersionRepository
import javax.inject.Inject

class GetLatestVersionUseCase @Inject constructor(
    private val versionRepository: VersionRepository
) {
    suspend operator fun invoke(): Result<Pair<String, String>> = runCatching {
        val currentVersion = versionRepository.getCurrentVersion()
            ?: return Result.failure(NullPointerException("Failed to load client application version"))

        val latestVersion = versionRepository.getLatestVersionFromPlayStore()
            ?: return Result.failure(NullPointerException("Failed to load latest application version"))

        currentVersion to latestVersion
    }
}