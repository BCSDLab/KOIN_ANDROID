package `in`.koreatech.koin.feature.lostandfound.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.feature.lostandfound.navigation.LostAndFoundNavType
import `in`.koreatech.koin.feature.lostandfound.navigation.koinLostAndFoundGraph
import javax.inject.Inject

@AndroidEntryPoint
class LostAndFoundActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                var startDestination by remember { mutableStateOf<LostAndFoundNavType>(LostAndFoundNavType.LostAndFoundListRoute) }
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = startDestination
                ) {
                    koinLostAndFoundGraph(
                        navController = navController,
                        navigator = navigator
                    )
                }
            }
        }
    }
}
