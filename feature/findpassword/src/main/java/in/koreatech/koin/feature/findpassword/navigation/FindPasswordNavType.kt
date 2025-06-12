package `in`.koreatech.koin.feature.findpassword.navigation

sealed class FindPasswordNavType(val route: String) {
    data object SmsVerification : FindPasswordNavType("sms_verification")
    data object EmailVerification : FindPasswordNavType("email_verification")
    data object ChangePassword : FindPasswordNavType("change_password")
    data object Complete : FindPasswordNavType("complete")
}
