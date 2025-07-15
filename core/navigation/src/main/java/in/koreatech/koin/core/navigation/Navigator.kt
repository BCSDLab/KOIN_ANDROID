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
        type: Pair<String, Any?> = Pair("", ""),
        navType: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToMain(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        targetBoardId: Pair<String, Any?> = Pair("", 0),
        targetArticleId: Pair<String, Any?> = Pair("", 0),
        targetChatId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToShop(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToDinging(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToArticle(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        targetBoardId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToArticleLostAndFound(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToChat(
        context: Context,
        targetArticleId: Pair<String, Any?> = Pair("", 0),
        targetChatId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToClubRecruitment(
        context: Context,
        targetClubId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", "")
    ): Intent
}
