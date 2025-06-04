package `in`.koreatech.koin.feature.club.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import `in`.koreatech.koin.feature.club.navigation.ClubNavType
import `in`.koreatech.koin.feature.club.navigation.koinClubGraph

@AndroidEntryPoint
class ClubActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val clubCategory = intent.getIntExtra(CATEGORY_ID, -1)

        setContent {
            KoinTheme {
                var startDestination by remember { mutableStateOf("${ClubNavType.ClubList.route}/$clubCategory") }
                val navController = rememberNavController()

                intent.getIntExtra(CLUB_ID, -1).let {
                    if (it != -1) {
                        startDestination = "${ClubNavType.ClubDetail.route}/$it"
                    }
                }

                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = startDestination
                ) {
                    koinClubGraph(
                        navController = navController
                    )
                }
            }
        }
    }
}
