package `in`.koreatech.koin.core.navigation

import android.content.Context
import android.content.Intent

@Suppress("TooManyFunctions")
interface Navigator {
    fun navigateToSplash(
        context: Context,
        type: Pair<String, Any?> = Pair("", ""),
        navType: Pair<String, Any?> = Pair("", ""),
        vararg args: Pair<String, Any?>
    ): Intent

    fun navigateTo(
        context: Context,
        type: Pair<String, String?> = Pair("", ""), // SchemeType
        vararg args: Pair<String, Any?> // Extra IDs
    ): Intent

    fun navigateToScheme(
        context: Context,
        extraUrl: String
    ): Intent

    fun navigateToSignIn(
        context: Context,
        redirectUrl: String? = null
    ): Intent

    fun navigateToNotificationSetting(
        context: Context
    ): Intent

    fun navigateToStore(
        context: Context
    ): Intent

    fun navigateToDining(
        context: Context
    ): Intent

    fun navigateToBusTimeTable(
        context: Context
    ): Intent

    fun navigateToBusSearch(
        context: Context
    ): Intent

    fun navigateToUnibus(
        context: Context
    ): Intent

    fun navigateToCallvan(
        context: Context
    ): Intent

    fun navigateToLand(
        context: Context
    ): Intent

    fun navigateToBusiness(
        context: Context
    ): Intent

    fun navigateToOperatingInfo(
        context: Context
    ): Intent

    fun navigateToTimetable(
        context: Context,
        isAnonymous: Boolean = false
    ): Intent

    fun navigateToLostAndFound(
        context: Context
    ): Intent

    fun navigateToChatRoom(
        context: Context
    ): Intent

    fun navigateToGroupChat(
        context: Context,
        extraPostId: Int
    ): Intent

    fun navigateToDepartmentInfo(
        context: Context
    ): Intent
}
