package `in`.koreatech.koin.navigation

import android.content.Context
import android.content.Intent
import `in`.koreatech.bus.BusSearchActivity
import `in`.koreatech.bus.BusTimetableActivity
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.EXTRA_POST_ID
import `in`.koreatech.koin.core.navigation.utils.buildDeepLinkIntent
import `in`.koreatech.koin.core.navigation.utils.buildIntent
import `in`.koreatech.koin.core.navigation.utils.isValidDeepLink
import `in`.koreatech.koin.feature.callvan.CallvanActivity
import `in`.koreatech.koin.feature.chat.ui.groupchat.GroupChatActivity
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity
import `in`.koreatech.koin.feature.dining.ui.DiningActivity
import `in`.koreatech.koin.feature.store.StoreActivity
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.ui.unibus.UnibusActivity
import javax.inject.Inject

@Suppress("TooManyFunctions")
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
        val className = SchemeType.fromType(type.second)?.className ?: return if (context.isValidDeepLink(type.second)) {
            buildDeepLinkIntent(type.second ?: "").apply {
                `package` = context.packageName
            }
        } else {
            context.buildIntent(MainActivity::class.java)
        }
        val intent = context.buildIntent(className, type, *args)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToSignIn(context: Context, redirectUrl: String?): Intent {
        return context.buildIntent(SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("link", redirectUrl)
        }
    }

    override fun navigateToNotificationSetting(context: Context): Intent {
        return context.buildIntent(NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToStore(context: Context): Intent {
        return context.buildIntent(StoreActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToDining(context: Context): Intent {
        return context.buildIntent(DiningActivity::class.java).apply {
            putExtra("url", "https://koreatech.unibus.kr/")
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToBusTimeTable(context: Context): Intent {
        return context.buildIntent(BusTimetableActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToBusSearch(context: Context): Intent {
        return context.buildIntent(BusSearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToUnibus(context: Context): Intent {
        return context.buildIntent(UnibusActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToCallvan(context: Context): Intent {
        return context.buildIntent(CallvanActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToChatRoom(context: Context): Intent {
        return context.buildIntent(ChatRoomActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToGroupChat(
        context: Context,
        extraPostId: Int
    ): Intent {
        return context.buildIntent(
            GroupChatActivity::class.java,
            EXTRA_POST_ID to extraPostId
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
