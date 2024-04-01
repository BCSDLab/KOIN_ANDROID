package `in`.koreatech.business.feature_signup.businessauth

sealed class BusinessAuthSideEffect {
    object ShowDialog : BusinessAuthSideEffect()
}