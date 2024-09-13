package `in`.koreatech.business.feature.signin

data class SignInState (
    val id: String = "",
    val password: String = "",
    val nullErrorMessage: String = "",
    val notValidateField: Boolean = false
)