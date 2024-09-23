package `in`.koreatech.business.feature.signin

data class SignInState (
    val id: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val notValidateField: Boolean = false
)