package `in`.koreatech.koin.core.navigation

import android.content.Context
import android.content.Intent

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

    fun navigateToChatRoom(
        context: Context
    ): Intent
}
