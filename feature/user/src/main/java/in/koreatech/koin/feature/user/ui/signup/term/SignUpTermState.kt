package `in`.koreatech.koin.feature.user.ui.signup.term

data class SignUpTermState(
    val privacyTerm: String = "",
    val koinTerm: String = "",
    val marketingTerm: String = "",
    val isPrivacyTermChecked: Boolean = false,
    val isKoinTermChecked: Boolean = false,
    val isMarketingTermChecked: Boolean = false
)
