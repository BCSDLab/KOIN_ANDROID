package `in`.koreatech.koin.domain.model.user

data class PasswordFormat(
    val isIncludeEnglish: Boolean,
    val isIncludeNumber: Boolean,
    val isIncludeSymbol: Boolean,
    val isValidLength: Boolean
)