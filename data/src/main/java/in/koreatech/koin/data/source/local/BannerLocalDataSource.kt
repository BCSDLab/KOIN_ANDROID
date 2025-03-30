package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.BannerDataStore
import javax.inject.Inject

class BannerLocalDataSource @Inject constructor(
    private val bannerDataStore: BannerDataStore
) {
    suspend fun getBannerRefusalDate(): Int {
        return bannerDataStore.getBannerRefusalDate()
    }

    suspend fun saveBannerRefusalDate(date: Int) {
        bannerDataStore.saveBannerRefusalDate(date)
    }
}
