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

    fun getLatestVersionFromPlayStore(): String? {
        val ele = Jsoup.connect(URLConstant.URL_PLAYSTORE)
            .timeout(5000)
            .get()
            .select("script.ds\\:5")
            .html()
        return REGEX_VERSION.find(ele)?.groupValues?.get(0)
    }

    private companion object {
        val REGEX_VERSION = """([0-9]\.[0-9]{1,2}\.[0-9]{1,2})""".toRegex()
    }
}