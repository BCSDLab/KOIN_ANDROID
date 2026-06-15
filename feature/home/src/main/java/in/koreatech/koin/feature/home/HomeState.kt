package `in`.koreatech.koin.feature.home

import `in`.koreatech.koin.feature.home.component.DiningPagerData
import `in`.koreatech.koin.feature.home.model.LocalWeather
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val diningData: ImmutableList<DiningPagerData> = persistentListOf(),
    val callVanRecruitCount: Int = 0,
    val shopEventForKoin: Int = 0,
    val openShopCount: Int = 0,
    val shopCount: Int = 0,
    val isNewNotificationReceived: Boolean = false,
    val username: String = "",
    val weather: LocalWeather? = null
)
