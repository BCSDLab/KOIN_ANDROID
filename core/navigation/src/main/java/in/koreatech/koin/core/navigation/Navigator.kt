package `in`.koreatech.koin.core.navigation

import android.content.Context
import android.content.Intent

interface Navigator {
    fun navigateToSplash(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        targetBoardId: Pair<String, Any?> = Pair("", 0),
        targetArticleId: Pair<String, Any?> = Pair("", 0),
        targetChatId: Pair<String, Any?> = Pair("", 0),
        targetClubId: Pair<String, Any?> = Pair("", 0),
        targetEventId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", ""),
        navType: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateTo(
        context: Context,
        type: Pair<String, String?> = Pair("", ""), // SchemeType
        vararg args: Pair<String, Any?> // Extra IDs
    ): Intent
}
