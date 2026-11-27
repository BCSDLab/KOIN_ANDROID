package `in`.koreatech.koin.navigation

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import `in`.koreatech.bus.BusSearchActivity
import `in`.koreatech.bus.BusTimetableActivity
import `in`.koreatech.koin.core.BuildConfig
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.utils.EXTRA_APPLICATION_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ARTICLE_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_CONTEXT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_DOMAIN_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_POST_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_RECRUITMENT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_URL
import `in`.koreatech.koin.core.navigation.utils.buildDeepLinkIntent
import `in`.koreatech.koin.core.navigation.utils.buildIntent
import `in`.koreatech.koin.core.navigation.utils.isValidDeepLink
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.feature.callvan.CallvanActivity
import `in`.koreatech.koin.feature.chat.ui.ChatActivity
import `in`.koreatech.koin.feature.department.DepartmentActivity
import `in`.koreatech.koin.feature.dining.ui.DiningActivity
import `in`.koreatech.koin.feature.land.LandActivity
import `in`.koreatech.koin.feature.lostandfound.ui.LostAndFoundActivity
import `in`.koreatech.koin.feature.recruitment.ui.RecruitmentActivity
import `in`.koreatech.koin.feature.store.StoreActivity
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.operating.OperatingInfoActivity
import `in`.koreatech.koin.ui.scheme.SchemeActivity
import `in`.koreatech.koin.ui.splash.SplashActivity
import `in`.koreatech.koin.ui.timetablev2.TimetableActivity
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
        val destination = SchemeType.fromType(type.second) ?: return if (context.isValidDeepLink(type.second)) {
            buildDeepLinkIntent(type.second ?: "").apply {
                `package` = context.packageName
            }
        } else {
            context.buildIntent(MainActivity::class.java)
        }
        val domainType = destination.chatDomainType
        if (domainType != null) {
            val contextKey = when (domainType) {
                ChatDomain.LOST_ITEM -> EXTRA_ARTICLE_ID
                ChatDomain.CALLVAN -> EXTRA_POST_ID
                ChatDomain.TEAM_RECRUITMENT -> EXTRA_RECRUITMENT_ID
            }
            var contextId: Int? = null
            var chatRoomId: Int? = null
            var applicationId: Int? = null
            for ((key, value) in args) {
                when (key) {
                    contextKey -> contextId = value as? Int
                    EXTRA_CHAT_ROOM_ID -> chatRoomId = value as? Int
                    EXTRA_APPLICATION_ID -> applicationId = value as? Int
                }
            }
            return navigateToChatRoom(context, domainType, requireNotNull(contextId), chatRoomId, applicationId)
        }
        val intent = context.buildIntent(destination.className, type, *args)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        return intent
    }

    override fun navigateToScheme(context: Context, extraUrl: String): Intent {
        return context.buildIntent(SchemeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_URL, extraUrl)
        }
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
            putExtra("url", "https://koreatech.unibus.kr/")
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToCallvan(context: Context): Intent {
        return context.buildIntent(CallvanActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToRecruitment(context: Context): Intent {
        return context.buildIntent(RecruitmentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToLand(context: Context): Intent {
        return context.buildIntent(LandActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToBusiness(context: Context): Intent {
        val ownerUrl = if (BuildConfig.IS_DEBUG || BuildConfig.IS_QA) {
            URLConstant.OWNER_URL_STAGE
        } else {
            URLConstant.OWNER_URL_PRODUCTION
        }
        return Intent(Intent.ACTION_VIEW, ownerUrl.toUri())
    }

    override fun navigateToOperatingInfo(context: Context): Intent {
        return context.buildIntent(OperatingInfoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToTimetable(context: Context, isAnonymous: Boolean): Intent {
        return context.buildIntent(TimetableActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(TimetableActivity.IS_ANONYMOUS, isAnonymous)
        }
    }

    override fun navigateToLostAndFound(context: Context): Intent {
        return context.buildIntent(LostAndFoundActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    override fun navigateToChatList(context: Context): Intent {
        return context.buildIntent(ChatActivity::class.java)
    }

    override fun navigateToChatRoom(
        context: Context,
        domainType: ChatDomain,
        contextId: Int,
        chatRoomId: Int?,
        applicationId: Int?
    ): Intent {
        if (domainType == ChatDomain.TEAM_RECRUITMENT && applicationId == null) {
            requireNotNull(chatRoomId)
        }
        return context.buildIntent(
            ChatActivity::class.java,
            EXTRA_CHAT_DOMAIN_TYPE to domainType.name,
            EXTRA_CHAT_CONTEXT_ID to contextId
        ).apply {
            chatRoomId?.let { putExtra(EXTRA_CHAT_ROOM_ID, it) }
            applicationId?.let { putExtra(EXTRA_APPLICATION_ID, it) }
        }
    }

    override fun navigateToDepartmentInfo(context: Context): Intent {
        return Intent(context, DepartmentActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
