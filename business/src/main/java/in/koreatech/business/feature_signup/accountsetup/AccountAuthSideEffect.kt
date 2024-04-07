package `in`.koreatech.business.feature_signup.accountsetup

sealed class AccountAuthSideEffect {
    data object NavigateToBackScreen : AccountAuthSideEffect()
    data class NavigateToNextScreen(val email: String) : AccountAuthSideEffect()
}