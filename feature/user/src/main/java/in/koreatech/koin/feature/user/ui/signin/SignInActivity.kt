package `in`.koreatech.koin.feature.user.ui.signin

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.feature.signin.ui.SignInScreen
import `in`.koreatech.koin.feature.user.DEEPLINK_MAIN
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingManager: OnboardingManager

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = KoinTheme.colors.neutral0
                ) { innerPadding ->
                    SignInScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        nextRouteTure = {
                            goToNextRoute(false)
                        },
                        nextRouteSignin = {
                            goToNextRoute(true)
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun goToNextRoute(isSignIn: Boolean) {
        val link = intent.extras?.getString("link")

        if (link != null) {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW).apply {
                        flags = FLAG_ACTIVITY_CLEAR_TOP
                        data = link.toUri()
                    }
                startActivity(intent)
            } catch (e: Exception) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_MAIN.toUri()
                }.let {
                    startActivity(it)
                }
            }
        } else {
            Intent(Intent.ACTION_VIEW).apply {
                data = DEEPLINK_MAIN.toUri()
            }.let {
                startActivity(it)
            }
        }

        if (isSignIn) {
            lifecycleScope.launch {
                with(onboardingManager) {
                    showModalIfNeeded(
                        actionTrue = { launchInfoRequiredActivity(true) },
                        actionFalse = { launchInfoRequiredActivity(false) }
                    )
                }
            }
        }
        finish()
    }

    private fun launchInfoRequiredActivity(check: Boolean) {
        val intent = Intent(Intent.ACTION_VIEW, INFO_REQUIRED_URI.toUri())
            .putExtra(EXTRA_IS_FULL, check)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this@SignInActivity.startActivity(intent)
        finishWithTransition()
    }

    private fun finishWithTransition() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
    }

    companion object {
        private const val INFO_REQUIRED_URI = "koin://inforequired/activity"
        private const val EXTRA_IS_FULL = "extra_is_full"
    }
}
