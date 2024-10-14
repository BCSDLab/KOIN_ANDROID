package `in`.koreatech.koin.navigation

import android.content.Context
import android.content.Intent
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.buildIntent
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.ui.store.activity.StoreActivity
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToSplash(
        context: Context,
        targetId: Pair<String, Any?>,
        type: Pair<String, Any?>,
        navType: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<SplashActivity>(targetId, type, navType)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToMain(
        context: Context,
        targetId: Pair<String, Any?>,
        type: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<MainActivity>(targetId, type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToShop(
        context: Context,
        targetId: Pair<String, Any?>,
        type: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<StoreActivity>(targetId, type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToDinging(
        context: Context,
        targetId: Pair<String, Any?>,
        type: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<DiningActivity>(targetId, type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToArticle(
        context: Context,
        targetId: Pair<String, Any?>,
        type: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<ArticleActivity>(targetId, type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }
}