package `in`.koreatech.koin.feature.findpassword.navigation

sealed class FindPasswordNavType(val route: String) {
    data object Verification : FindPasswordNavType("verification")
    data object ChangePassword : FindPasswordNavType("change_password")
    data object Complete : FindPasswordNavType("complete")
}
