package `in`.koreatech.koin.feature.setting.navigation

sealed class SettingNavType(val route: String) {
    data object Setting : SettingNavType("Setting")
}
