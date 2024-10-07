package `in`.koreatech.koin.domain.repository

interface VersionRepository {
    suspend fun getCurrentVersion(): String?
    suspend fun getLatestVersionFromRemote(): String

    suspend fun updateLatestVersionCode(versionCode: Int)
    suspend fun updateLatestVersionName(versionName: String)
    suspend fun getLatestVersionCode(): Int?
    suspend fun getLatestVersionName(): String?
    suspend fun getCurrentVersionCode(): Int?
    suspend fun getCurrentVersionName(): String?
}