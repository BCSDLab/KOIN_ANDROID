package `in`.koreatech.koin.feature.user.ui.findid.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class FindIdNavType {
    @Serializable
    data object Verification : FindIdNavType()

    @Serializable
    data class Complete(val loginId: String) : FindIdNavType()
}
