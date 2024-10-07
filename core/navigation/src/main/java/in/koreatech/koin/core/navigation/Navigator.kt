package `in`.koreatech.koin.core.navigation

import android.content.Context
import android.content.Intent

interface Navigator {
    fun navigateToSplash(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", ""),
        navType: Pair<String, Any?> = Pair("", "")
    ): Intent

    fun navigateToMain(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", ""),
    ): Intent

    fun navigateToShop(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", ""),
    ): Intent

    fun navigateToDinging(
        context: Context,
        targetId: Pair<String, Any?> = Pair("", 0),
        type: Pair<String, Any?> = Pair("", ""),
    ): Intent
}