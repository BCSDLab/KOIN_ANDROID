package `in`.koreatech.koin.feature.club.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import `in`.koreatech.koin.feature.club.navigation.ClubNavType
import `in`.koreatech.koin.feature.club.navigation.koinClubGraph

@AndroidEntryPoint
class ClubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val clubCategory = intent.getIntExtra(CATEGORY_ID, -1).takeIf {
            it != -1
        }

        setContent {
            KoinTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = "${ClubNavType.ClubList.route}/$clubCategory"
                ) {
                    koinClubGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}
