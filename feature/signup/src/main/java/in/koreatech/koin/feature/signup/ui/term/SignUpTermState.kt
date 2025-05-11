package `in`.koreatech.koin.feature.signup.ui.term

data class SignUpTermState(
    val privacyTerm: String = "",
    val koinTerm: String = "",
    val marketingTerm: String = "",
    val isPrivacyTermChecked: Boolean = false,
    val isKoinTermChecked: Boolean = false,
    val isMarketingTermChecked: Boolean = false
)
