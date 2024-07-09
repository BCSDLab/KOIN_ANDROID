package `in`.koreatech.business.feature.signup.businessauth

import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupSideEffect

sealed class BusinessAuthSideEffect {
    data object NavigateToSearchStore : BusinessAuthSideEffect()
    data object NavigateToBackScreen : BusinessAuthSideEffect()
    data object NavigateToNextScreen : BusinessAuthSideEffect()
}