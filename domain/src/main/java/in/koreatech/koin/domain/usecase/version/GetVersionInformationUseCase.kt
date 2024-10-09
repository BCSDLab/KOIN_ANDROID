package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.repository.VersionRepository
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import javax.inject.Inject

class GetVersionInformationUseCase @Inject constructor(
    private val versionRepository: VersionRepository
) {
    suspend operator fun invoke(): Result<Version> {
        val currentVersion = try {
            versionRepository.getCurrentVersion()
        } catch (t: Throwable) {
            return Result.failure(t)
        } ?: return Result.failure(NullPointerException("Failed to load application version: null"))

        val version = try {
            versionRepository.getLatestVersionFromRemote()
        } catch (t: Throwable) {
            return Result.failure(t)
        }

        return runCatching {
            val (currentMajor, currentMinor, currentPath) = currentVersion.split(".")
                .map { it.toInt() }
            val (latestMajor, latestMinor, latestPath) = version.latestVersion.split(".")
                .map { it.toInt() }

            when {
                currentMajor < latestMajor ||
                currentMajor == latestMajor && currentMinor < latestMinor ||
                currentMajor == latestMajor && currentMinor == latestMinor && currentPath < latestPath ->
                    Version(currentVersion, version.latestVersion, version.title, version.content, VersionUpdatePriority.Importance)
                else ->
                    Version(currentVersion, version.latestVersion, version.title, version.content, VersionUpdatePriority.None)
            }
        }
    }
}