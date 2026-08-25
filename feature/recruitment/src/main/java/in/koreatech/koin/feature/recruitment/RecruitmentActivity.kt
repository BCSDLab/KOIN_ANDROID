package `in`.koreatech.koin.feature.recruitment

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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
        }

        setContent {
            RebrandKoinTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .background(color = RebrandKoinTheme.colors.neutral50)
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        navController = navController,
                        startDestination = RecruitmentNavType.RecruitmentMain
                    ) {
                        koinRecruitmentGraph(navController = navController)
                    }
                }
            }
        }
    }
}
