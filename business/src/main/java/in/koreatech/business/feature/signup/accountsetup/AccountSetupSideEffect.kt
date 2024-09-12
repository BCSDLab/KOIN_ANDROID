package `in`.koreatech.business.feature.signup.accountsetup

sealed class AccountSetupSideEffect {
    data object NavigateToBackScreen : AccountSetupSideEffect()
    data object NavigateToNextScreen : AccountSetupSideEffect()
}