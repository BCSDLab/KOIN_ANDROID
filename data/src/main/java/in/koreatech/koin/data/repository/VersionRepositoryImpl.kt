package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import `in`.koreatech.koin.data.source.remote.VersionRemoteDataSource
import `in`.koreatech.koin.domain.repository.VersionRepository
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val versionLocalDataSource: VersionLocalDataSource,
    private val versionRemoteDataSource: VersionRemoteDataSource
) : VersionRepository {
    override suspend fun getCurrentVersion(): String? {
        return versionLocalDataSource.getVersion()
    }

    override suspend fun getLatestVersionFromRemote(): String {
        return versionRemoteDataSource.getAndroidAppVersion().version
    }
}