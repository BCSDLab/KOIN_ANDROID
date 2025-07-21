package `in`.koreatech.koin.feature.user.ui.findid

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.findid.navigation.FindIdNavType
import `in`.koreatech.koin.feature.user.ui.findid.navigation.koinFindIdNavigation

@AndroidEntryPoint
class FindIdActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        KoinTopAppBar(
                            title = stringResource(R.string.find_id_title),
                            onNavigationIconClick = {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    },
                    containerColor = KoinTheme.colors.neutral0,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { contentPadding ->
                    NavHost(
                        modifier = Modifier
                            .padding(contentPadding),
                        navController = navController,
                        enterTransition = {
                            EnterTransition.None
                        },
                        exitTransition = {
                            ExitTransition.None
                        },
                        startDestination = FindIdNavType.Verification.route
                    ) {
                        koinFindIdNavigation(navController = navController)
                    }
                }
            }
        }
    }
}
