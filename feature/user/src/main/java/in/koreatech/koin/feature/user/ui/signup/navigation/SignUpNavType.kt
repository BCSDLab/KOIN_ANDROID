package `in`.koreatech.koin.feature.user.ui.signup.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SignUpNavType {
    @Serializable
    data object Term : SignUpNavType()

    @Serializable
    data object Verification : SignUpNavType()

    @Serializable
    data class UserType(
        val name: String,
        val phoneNumber: String,
        val gender: String
    ) : SignUpNavType()

    @Serializable
    data class StudentUserInfo(
        val name: String,
        val phoneNumber: String,
        val gender: String
    ) : SignUpNavType()

    @Serializable
    data class GeneralUserInfo(
        val name: String,
        val phoneNumber: String,
        val gender: String
    ) : SignUpNavType()

    @Serializable
    data object SignUpComplete : SignUpNavType()
}
