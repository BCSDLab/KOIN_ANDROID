package `in`.koreatech.koin.navigation

import android.content.Context
import android.content.Intent
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.buildIntent
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.splash.SplashActivity
import javax.inject.Inject
import kotlin.jvm.java

class NavigatorImpl @Inject constructor() : Navigator {
    override fun navigateToSplash(
        context: Context,
        type: Pair<String, Any?>,
        navType: Pair<String, Any?>,
        vararg args: Pair<String, Any?>
    ): Intent {
        val intent = context.buildIntent<SplashActivity>(type, navType, *args)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateTo(
        context: Context,
        type: Pair<String, String?>,
        vararg args: Pair<String, Any?>
    ): Intent {
        val className = SchemeType.fromType(type.second)?.className ?: return context.buildIntent(MainActivity::class.java)
        val intent = context.buildIntent(className, type, *args)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }
}
