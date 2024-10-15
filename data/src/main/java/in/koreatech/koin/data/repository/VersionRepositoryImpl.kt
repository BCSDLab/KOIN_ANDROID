package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import `in`.koreatech.koin.data.source.remote.VersionRemoteDataSource
import `in`.koreatech.koin.domain.model.version.Version
import `in`.koreatech.koin.domain.repository.VersionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val versionLocalDataSource: VersionLocalDataSource,
    private val versionRemoteDataSource: VersionRemoteDataSource,
) : VersionRepository {
    override suspend fun getCurrentVersion(): String? {
        return versionLocalDataSource.getCurrentVersionName()
    }

    override suspend fun getLatestVersionFromRemote(): Version {
        return versionRemoteDataSource.getAndroidAppVersion().toVersion()
    }
     
    override suspend fun updateLatestVersionCode(versionCode: Int) {
        versionLocalDataSource.updateLatestVersionCode(versionCode)
    }

    override suspend fun updateLatestVersionName(versionName: String) {
        versionLocalDataSource.updateLatestVersionName(versionName)
    }

    override suspend fun getLatestVersionCode(): Int? {
        return versionLocalDataSource.getLatestVersionCode()
    }

    override suspend fun getLatestVersionName(): String? {
        return versionLocalDataSource.getLatestVersionName()
    }

    override suspend fun getCurrentVersionCode(): Int? {
        return versionLocalDataSource.getCurrentVersionCode()
    }

    override suspend fun getCurrentVersionName(): String? {
        return versionLocalDataSource.getCurrentVersionName()
    }
}