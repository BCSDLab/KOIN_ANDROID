package `in`.koreatech.business.feature.signup.checkterm

data class CheckTermState(
    val isAllTermChecked: Boolean = false,
    val isCheckedPrivacyTerms: Boolean = false,
    val isCheckedKoinTerms: Boolean = false
)