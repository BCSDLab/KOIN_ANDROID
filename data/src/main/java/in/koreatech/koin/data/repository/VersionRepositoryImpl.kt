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
    private val coroutineScope: CoroutineScope
) : VersionRepository {

    private val _latestVersion: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        fetchingLatestVersionFromPlayStore()
    }

    override suspend fun getCurrentVersion(): String? {
        return versionLocalDataSource.getVersion()
    }

    override suspend fun getLatestVersionFromRemote(): Version {
        return versionRemoteDataSource.getAndroidAppVersion().toVersion()
    }

    override suspend fun getLatestVersionFromPlayStore(): String? {
        return _latestVersion.value?.also {
            fetchingLatestVersionFromPlayStore()
        }
    }

    private fun fetchingLatestVersionFromPlayStore() {
        flow { emit(versionRemoteDataSource.getLatestVersionFromPlayStore()) }
            .retry(3)
            .catch { emit(null) }
            .onEach { _latestVersion.value = it }
            .launchIn(coroutineScope)
    }
}