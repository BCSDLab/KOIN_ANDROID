package `in`.koreatech.koin.feature.signup.navigation

sealed class SignUpNavType(val route: String) {
    data object Term : SignUpNavType("signUpTerm")
    data object Verification : SignUpNavType("signUpVerification")
    data object UserType : SignUpNavType("signUpUsertype")
    data object StudentUserInfo : SignUpNavType("signUpStudentUserinfo")
    data object GeneralUserInfo : SignUpNavType("signUpGeneralUserinfo")
    data object SignUpComplete : SignUpNavType("signUpComplete")
}
