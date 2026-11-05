package `in`.koreatech.koin.feature.user.ui.signup.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class SignUpNavType {
    @Serializable
    data class Term(
        val sessionId: String
    ) : SignUpNavType()

    @Serializable
    data class Verification(
        val sessionId: String
    ) : SignUpNavType()

    @Serializable
    data class UserType(
        val name: String,
        val phoneNumber: String,
        val gender: String,
        val sessionId: String
    ) : SignUpNavType()

    @Serializable
    data class StudentUserInfo(
        val name: String,
        val phoneNumber: String,
        val gender: String,
        val sessionId: String
    ) : SignUpNavType()

    @Serializable
    data class GeneralUserInfo(
        val name: String,
        val phoneNumber: String,
        val gender: String,
        val sessionId: String
    ) : SignUpNavType()

    @Serializable
    data object SignUpComplete : SignUpNavType()
}
