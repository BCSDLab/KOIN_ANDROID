package `in`.koreatech.koin.feature.user.ui.findpassword.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class FindPasswordNavType {
    @Serializable
    data object Verification : FindPasswordNavType()

    @Serializable
    data class ChangePassword(
        val loginId: String,
        val verificationMethod: String
    ) : FindPasswordNavType()

    @Serializable
    data object Complete : FindPasswordNavType()
}
