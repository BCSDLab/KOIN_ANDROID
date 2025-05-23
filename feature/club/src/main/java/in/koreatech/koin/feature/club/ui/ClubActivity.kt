package `in`.koreatech.koin.feature.club.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.club.navigation.ClubNavType
import `in`.koreatech.koin.feature.club.navigation.koinClubGraph

class ClubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = ClubNavType.ClubDetail.route
                ) {
                    koinClubGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}
