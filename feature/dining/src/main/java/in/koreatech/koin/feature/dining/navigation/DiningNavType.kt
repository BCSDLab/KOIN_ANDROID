package `in`.koreatech.koin.feature.dining.navigation

sealed class DiningNavType(val route: String) {
    data object DiningDetail: DiningNavType("dining_detail")
    data object DiningNotice: DiningNavType("dining_notice")
}