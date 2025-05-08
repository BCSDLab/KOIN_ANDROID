package `in`.koreatech.koin.feature.signup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.consumeWindowInsets
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
import `in`.koreatech.koin.feature.signup.navigation.SignUpNavType
import `in`.koreatech.koin.feature.signup.navigation.koinSignUpGraph

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        KoinTopAppBar(
                            title = stringResource(R.string.sign_up_title),
                            onNavigationIconClick = {
                                if (!navController.popBackStack()) {
                                    finish()
                                }
                            }
                        )
                    },
                    containerColor = KoinTheme.colors.neutral0
                ) { contentPadding ->
                    NavHost(
                        modifier = Modifier
                            .padding(contentPadding)
                            .consumeWindowInsets(contentPadding),
                        navController = navController,
                        enterTransition = {
                            EnterTransition.None
                        },
                        exitTransition = {
                            ExitTransition.None
                        },
                        startDestination = SignUpNavType.Term.route
                    ) {
                        koinSignUpGraph(navController = navController)
                    }
                }
            }
        }
    }
}
