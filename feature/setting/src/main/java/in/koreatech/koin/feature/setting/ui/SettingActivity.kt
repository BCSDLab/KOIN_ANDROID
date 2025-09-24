package `in`.koreatech.koin.feature.setting.ui

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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar
import `in`.koreatech.koin.feature.setting.navigation.SettingNavType
import `in`.koreatech.koin.feature.setting.navigation.koinSettingGraph

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithDarkStatusBar()

        setContent {
            KoinTheme {
                var startDestination by remember { mutableStateOf(SettingNavType.Setting.route) }
                navController = rememberNavController()
                NavHost(
                    modifier = Modifier.Companion,
                    navController = navController,
                    startDestination = startDestination
                ) {
                    koinSettingGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}
