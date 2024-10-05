package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.version.Version

interface VersionRepository {
    suspend fun getCurrentVersion(): String?
    suspend fun getLatestVersionFromPlayStore(): String?
    suspend fun getLatestVersionFromRemote(): Version
}