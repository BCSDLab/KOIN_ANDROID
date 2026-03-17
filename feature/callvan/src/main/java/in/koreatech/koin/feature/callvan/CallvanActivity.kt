package `in`.koreatech.koin.feature.callvan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.callvan.navigation.CallvanNavType
import `in`.koreatech.koin.feature.callvan.navigation.koinCallvanGraph

@AndroidEntryPoint
class CallvanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        var startDestination: CallvanNavType = CallvanNavType.CallvanMain
        if (intent.getBooleanExtra(EXTRA_NAVIGATE_TO_CREATE, false)) {
            startDestination = CallvanNavType.CallvanCreate
        }
        setContent {
            RebrandKoinTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
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
