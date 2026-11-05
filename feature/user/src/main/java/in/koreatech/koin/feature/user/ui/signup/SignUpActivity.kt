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
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.domain.usecase.session.GetSessionIdUseCase
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.signup.navigation.SignUpNavType
import `in`.koreatech.koin.feature.user.ui.signup.navigation.koinUserGraph
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    @Inject
    lateinit var getSessionIdUseCase: GetSessionIdUseCase

    private lateinit var sessionId: String

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionId = savedInstanceState?.getString(STATE_SESSION_ID)
            ?: intent.getStringExtra(EXTRA_SESSION_ID)
            ?: getSessionIdUseCase(
                sessionName = "sign_up",
                isLoggedIn = false,
                shouldExpireOtherSessions = true
            )
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
                        KoinTopAppBar2(
                            title = {
                                Text(
                                    text = stringResource(R.string.sign_up_title)
                                )
                            },
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
                        startDestination = SignUpNavType.Term(sessionId = sessionId)
                    ) {
                        koinUserGraph(navController = navController, finish = { finish() })
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SESSION_ID, sessionId)
    }

    companion object {
        const val EXTRA_SESSION_ID = "extra_session_id"
        private const val STATE_SESSION_ID = "state_session_id"
    }
}
