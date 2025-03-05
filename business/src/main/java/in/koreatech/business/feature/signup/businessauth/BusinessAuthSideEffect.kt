package `in`.koreatech.business.feature.signup.businessauth

sealed class BusinessAuthSideEffect {
    data object NavigateToSearchStore : BusinessAuthSideEffect()

    data object NavigateToBackScreen : BusinessAuthSideEffect()

    data object NavigateToNextScreen : BusinessAuthSideEffect()
}
