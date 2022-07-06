package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.model.version.VersionUpdatePriority
import `in`.koreatech.koin.domain.repository.VersionRepository
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

        val latestVersion = try {
            versionRepository.getLatestVersionFromRemote()
        } catch (t: Throwable) {
            return Result.failure(t)
        }

        return kotlin.runCatching {
            val (currentMajor, currentMinor, currentPoint) = currentVersion.split(".")
                .map { it.toInt() }
            val (latestMajor, latestMinor, latestPoint) = latestVersion.split(".")
                .map { it.toInt() }

            when {
                currentMajor < latestMajor ->
                    Version(currentVersion, latestVersion, VersionUpdatePriority.High)
                currentMajor == latestMajor && currentMinor < latestMinor ->
                    Version(currentVersion, latestVersion, VersionUpdatePriority.Medium)
                currentMajor == latestMajor && currentMinor == latestMinor && currentPoint < latestPoint ->
                    Version(currentVersion, latestVersion, VersionUpdatePriority.Low)
                else ->
                    Version(currentVersion, latestVersion, VersionUpdatePriority.None)
            }
        }
    }
}