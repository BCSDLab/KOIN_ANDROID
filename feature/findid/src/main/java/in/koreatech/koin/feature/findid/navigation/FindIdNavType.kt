package `in`.koreatech.koin.feature.findid.navigation

sealed class FindIdNavType(val route: String) {
    data object Verification : FindIdNavType("verification")
    data object Complete : FindIdNavType("complete")
}
