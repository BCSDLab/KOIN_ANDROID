package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.VersionApi
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.VersionResponse
import org.jsoup.Jsoup
import javax.inject.Inject

class VersionRemoteDataSource @Inject constructor(
    private val versionApi: VersionApi
) {
    suspend fun getAndroidAppVersion(): VersionResponse {
        return versionApi.getVersion("android")
    }
}