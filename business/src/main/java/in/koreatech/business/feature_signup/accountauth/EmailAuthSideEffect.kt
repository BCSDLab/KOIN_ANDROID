package `in`.koreatech.business.feature_signup.accountauth

sealed class EmailAuthSideEffect {
    data object NavigateToBackScreen : EmailAuthSideEffect()
    data object NavigateToNextScreen : EmailAuthSideEffect()
}