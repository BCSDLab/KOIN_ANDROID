package `in`.koreatech.koin.feature.recruitment.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.recruitment.navigation.RecruitmentNavType
import `in`.koreatech.koin.feature.recruitment.navigation.koinRecruitmentGraph

@AndroidEntryPoint
class RecruitmentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
            // 투명 테마 Activity에서 방향 고정 시 발생하는 예외를 무시
        }

        setContent {
            RebrandKoinTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = RecruitmentNavType.MyRecruitment
                ) {
                    koinRecruitmentGraph(navController = navController)
                }
            }
        }
    }
}
