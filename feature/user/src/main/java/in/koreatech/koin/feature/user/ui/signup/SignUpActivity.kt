package `in`.koreatech.koin.feature.user.ui.signup

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import `in`.koreatech.koin.feature.user.ui.signup.navigation.SignUpNavType
import `in`.koreatech.koin.feature.user.ui.signup.navigation.koinUserGraph

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
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
                            title = stringResource(R.string.sign_up_title),
                            onNavigationIconClick = {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    },
                    containerColor = KoinTheme.colors.neutral0
                ) { contentPadding ->
                    /*
                     * On Compose material 3, Scaffold content padding adds unnecessary padding when ime up
                     * So, let's consume window insets and add extra system bar padding
                     * see: https://issuetracker.google.com/issues/249727298
                     */
                    NavHost(
                        modifier = Modifier
                            .padding(contentPadding)
                            .consumeWindowInsets(contentPadding)
                            .systemBarsPadding(),
                        navController = navController,
                        enterTransition = {
                            EnterTransition.None
                        },
                        exitTransition = {
                            ExitTransition.None
                        },
                        startDestination = SignUpNavType.Term.route
                    ) {
                        koinUserGraph(navController = navController, finish = { finish() })
                    }
                }
            }
        }
    }
}
