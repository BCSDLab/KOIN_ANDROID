package `in`.koreatech.koin.feature.callvan

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.feature.callvan.navigation.CallvanNavType
import `in`.koreatech.koin.feature.callvan.navigation.koinCallvanGraph

@AndroidEntryPoint
class CallvanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }

        var startDestination: CallvanNavType = CallvanNavType.CallvanMain
        if (intent.getBooleanExtra(EXTRA_NAVIGATE_TO_CREATE, false)) {
            startDestination = CallvanNavType.CallvanCreate
        }

        val targetId = intent.getIntExtra(EXTRA_ID, -1)

        setContent {
            RebrandKoinTheme {
                val navController = rememberNavController()

                // Scheme url from backend is not matching with deeplink url in navigation
                // So, let's navigate to correct deeplink from here
                LaunchedEffect(Unit) {
                    if (targetId == -1) return@LaunchedEffect
                    navController.navigate(CallvanNavType.CallvanDetail(targetId))
                }

                Scaffold { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .background(color = RebrandKoinTheme.colors.neutral0)
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        koinCallvanGraph(navController = navController)
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_NAVIGATE_TO_CREATE = "navigate_to_create"
    }
}
