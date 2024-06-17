package `in`.koreatech.business.feature.signup.checkterm

sealed class CheckTermSideEffect {
    data object NavigateToNextScreen : CheckTermSideEffect()
    data object NavigateToBackScreen : CheckTermSideEffect()
}