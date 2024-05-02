package `in`.koreatech.business.feature.signup.accountauth

sealed class EmailAuthSideEffect {
    data object NavigateToBackScreen : EmailAuthSideEffect()
    data class NavigateToNextScreen(val email:String) : EmailAuthSideEffect()
}