package `in`.koreatech.business.feature.signup.businessauth

sealed class BusinessAuthSideEffect {
    object ShowDialog : BusinessAuthSideEffect()
}