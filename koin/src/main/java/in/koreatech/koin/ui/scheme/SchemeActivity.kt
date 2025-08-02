package `in`.koreatech.koin.ui.scheme

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.NavigatorType
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ARTICLE_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_BOARD_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CLUB_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_EVENT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_NAV_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_URL
import `in`.koreatech.koin.core.navigation.utils.toHost
import `in`.koreatech.koin.databinding.ActivitySchemeBinding
import `in`.koreatech.koin.navigation.SchemeType
import `in`.koreatech.koin.ui.main.activity.MainActivity
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class SchemeActivity : ActivityBase() {
    @Inject
    lateinit var navigator: Navigator

    companion object {
        private const val screenTitle = "스킴화면"
    }

    override val screenTitle: String
        get() = SchemeActivity.screenTitle
    private lateinit var binding: ActivitySchemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        intent.getStringExtra(EXTRA_URL).let { url ->
            val tasks =
                getSystemService<ActivityManager>()?.appTasks?.map {
                    /**
                     * class Name : in.koreatech.koin.ui.main.activity.MainActivity
                     */
                    it.taskInfo.baseActivity?.className?.equals(MainActivity::class.qualifiedName) == true
                }

            val navigatorType =
                tasks?.any { it }.let {
                    if (it == true) {
                        NavigatorType.DETAIL
                    } else {
                        NavigatorType.MAIN
                    }
                }
            Timber.e("navigatorType : $navigatorType")
            Timber.e("url : $url")
            Timber.e("url to host : ${url?.toHost()}")

            /**
             * koin://shop
             * url?.toHost = shop
             */
            when (navigatorType) {
                NavigatorType.MAIN -> {
                    val intent =
                        navigator.navigateToSplash(
                            context = this,
                            type = Pair(EXTRA_TYPE, url?.toHost()),
                            navType = Pair(EXTRA_NAV_TYPE, NavigatorType.MAIN.type),
                            *(getExtraIds(url ?: "").toTypedArray())
                        )
                    navigateToActivity(intent)
                }

                NavigatorType.DETAIL -> {
                    logging(url)
                    val intent = navigator.navigateTo(
                        context = this,
                        type = Pair(EXTRA_TYPE, url?.toHost()),
                        *(getExtraIds(url ?: "").toTypedArray())
                    )
                    navigateToActivity(intent)
                }
            }
        }
    }

    private fun logging(url: String?) {
        when (url?.toHost()) {
            SchemeType.ARTICLE.type -> {
                EventLogger.logNotificationEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.KEYWORD_NOTIFICATION,
                    getKeywordFromUrl(url)
                )
            }
        }
    }

    private fun getExtraIds(url: String): List<Pair<String, Any?>> {
        return listOf(
            Pair(EXTRA_ID, getIdFromUrl(url)),
            Pair(EXTRA_ARTICLE_ID, getArticleIdFromUrl(url)),
            Pair(EXTRA_CHAT_ROOM_ID, getChatRoomIdFromUrl(url)),
            Pair(EXTRA_BOARD_ID, getBoardIdFromUrl(url)),
            Pair(EXTRA_CLUB_ID, getClubIdFromUrl(url)),
            Pair(EXTRA_EVENT_ID, getEventIdFromUrl(url))
        ).filter { it.second != null }
    }

    private fun getIdFromUrl(url: String): Int? {
        return Uri.parse(url).getQueryParameter("id")?.toIntOrNull()
    }

    private fun getArticleIdFromUrl(url: String): Int {
        return Uri.parse(url).getQueryParameter("articleId")?.toIntOrNull() ?: -1
    }

    private fun getChatRoomIdFromUrl(url: String): Int {
        return Uri.parse(url).getQueryParameter("chatRoomId")?.toIntOrNull() ?: -1
    }

    private fun getBoardIdFromUrl(url: String): Int {
        return Uri.parse(url).getQueryParameter("board-id")?.toIntOrNull() ?: -1
    }

    private fun getKeywordFromUrl(url: String): String {
        return Uri.parse(url).getQueryParameter("keyword") ?: ""
    }

    private fun getClubIdFromUrl(url: String): Int {
        return Uri.parse(url).getQueryParameter("clubId")?.toIntOrNull() ?: -1
    }

    private fun getEventIdFromUrl(url: String): Int {
        return Uri.parse(url).getQueryParameter("eventId")?.toIntOrNull() ?: -1
    }

    private fun navigateToActivity(intent: Intent) {
        startActivity(intent)
        finish()
    }
}
