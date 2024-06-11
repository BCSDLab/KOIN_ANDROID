package `in`.koreatech.business.feature.signup.completesignup

sealed class CompleteSignupSideEffect {
    data object NavigateToLoginScreen : CompleteSignupSideEffect()
    data object NavigateToBackScreen : CompleteSignupSideEffect()
}