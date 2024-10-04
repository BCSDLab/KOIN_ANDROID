package `in`.koreatech.koin.domain.repository

interface VersionRepository {
    suspend fun getCurrentVersion(): String?
    suspend fun getLatestVersionFromRemote(): String
    suspend fun getLatestVersionFromPlayStore(): String?
}