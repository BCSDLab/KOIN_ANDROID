package `in`.koreatech.koin.feature.callvan.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class CallvanNavType {
    @Serializable
    data object CallvanMain : CallvanNavType()

    @Serializable
    data class CallvanDetail(val postId: Int) : CallvanNavType()

    @Serializable
    data object CallvanCreate : CallvanNavType()

    @Serializable
    data class CallvanChat(val postId: Int) : CallvanNavType()

    @Serializable
    data object CallvanNotifications : CallvanNavType()

    @Serializable
    data class CallvanReport(val postId: Int, val reportedUserId: Int) : CallvanNavType()
}
