package `in`.koreatech.koin.domain.repository

import javax.inject.Inject

interface VersionRepository {
    suspend fun getCurrentVersion(): String?
    suspend fun getLatestVersionFromRemote(): String
}