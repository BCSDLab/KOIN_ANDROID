package `in`.koreatech.koin.feature.dining.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.DiningAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.dining.constants.PARAMS_DATE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_TYPE
import `in`.koreatech.koin.feature.dining.navigation.DiningNavType
import `in`.koreatech.koin.feature.dining.navigation.koinDiningGraph
import javax.inject.Inject

private const val KOIN_SCHEME = "koin"

private data class DiningNavigationRequest(
    val requestId: Long,
    val initDate: String,
    val initTabType: Int
)

@AndroidEntryPoint
class DiningActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    private val pendingNavigationRequest = mutableStateOf<DiningNavigationRequest?>(null)
    private var navigationRequestSequence = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination: Any = if (Intent.ACTION_VIEW == intent.action) {
            val initPair = onActionView()
            DiningNavType.DiningDetail(initDate = initPair.first, initTabType = initPair.second)
        } else {
            DiningNavType.DiningDetail()
        }

        setContent {
            RebrandKoinTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = startDestination
                ) {
                    koinDiningGraph(
                        navController = navController
                    )
                }

                LaunchedEffect(pendingNavigationRequest.value?.requestId) {
                    val pendingRequest = pendingNavigationRequest.value ?: return@LaunchedEffect
                    pendingNavigationRequest.value = null
                    navController.navigate(
                        DiningNavType.DiningDetail(
                            initDate = pendingRequest.initDate,
                            initTabType = pendingRequest.initTabType
                        )
                    ) {
                        popUpTo<DiningNavType.DiningDetail> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    private fun onActionView(): Pair<String, Int> {
        val data = intent.data
        if (data?.scheme == KOIN_SCHEME) {
            return Pair(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()), -1)
        }
        EventLogger.logClickEvent(
            EventAction.DA1,
            DiningAnalyticsConstant.Label.KAKAO_ENTRY,
            DiningAnalyticsConstant.Value.KAKAO_ENTRY
        )
        var initDate = TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())
        var initTabType = 0
        data?.let {
            it.getQueryParameter(PARAMS_DATE)?.let { dateString ->
                initDate = TimeUtil.dateFormatToYYMMDD(TimeUtil.stringToDateYYYYMMDD(dateString))
            }

            it.getQueryParameter(PARAMS_TYPE)?.let { type ->
                initTabType = when (DiningUtil.getTypeByString(type)) {
                    DiningType.Breakfast -> 0
                    DiningType.Lunch -> 1
                    DiningType.Dinner -> 2
                    DiningType.NextBreakfast -> 0
                }
            }
        }
        return Pair(initDate, initTabType)
    }

    override fun onNewIntent(intent: Intent) {
        intent.apply {
            this.action = Intent.ACTION_VIEW
            setIntent(this)
        }
        super.onNewIntent(intent)
        val initPair = onActionView()
        pendingNavigationRequest.value = DiningNavigationRequest(
            requestId = ++navigationRequestSequence,
            initDate = initPair.first,
            initTabType = initPair.second
        )
    }
}
