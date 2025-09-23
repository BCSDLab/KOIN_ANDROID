package `in`.koreatech.koin.feature.dining.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.dining.constants.PARAMS_DATE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_TYPE
import `in`.koreatech.koin.feature.dining.navigation.DiningNavType
import `in`.koreatech.koin.feature.dining.navigation.koinDiningGraph
import androidx.core.net.toUri

@AndroidEntryPoint
class DiningActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithDarkStatusBar()

        setContent {
            KoinTheme {
                var startDestination by remember { mutableStateOf(DiningNavType.DiningDetail.route) }
                navController = rememberNavController()

                if (Intent.ACTION_VIEW == intent.action) {
                    val initPair = onActionView()
                    startDestination = "${DiningNavType.DiningDetail.route}?initDate=${initPair.first}&initTabType=${initPair.second}"
                }

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = startDestination
                ) {
                    koinDiningGraph(
                        navController = navController,
                        onNavigateToStore = {
                            val uri = "koin://store".toUri()
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun onActionView(): Pair<String, Int> {
        EventLogger.logClickEvent(
            EventAction.CAMPUS,
            AnalyticsConstant.Label.MENU_SHARE,
            "코인으로 이동"
        )
        var initDate = TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())
        var initTabType = 0
        val data = intent.data
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
        val startDestination = "${DiningNavType.DiningDetail.route}?initDate=${initPair.first}&initTabType=${initPair.second}"
        navController.navigate(startDestination) {
            popUpTo(DiningNavType.DiningDetail.route) { inclusive = true }
            launchSingleTop = true
        }
    }
}
