package `in`.koreatech.koin.feature.club.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import `in`.koreatech.koin.feature.club.navigation.ClubNavType
import `in`.koreatech.koin.feature.club.navigation.EXTRA_CLUB_ID
import `in`.koreatech.koin.feature.club.navigation.EXTRA_EVENT_ID
import `in`.koreatech.koin.feature.club.navigation.koinClubGraph

@AndroidEntryPoint
class ClubActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }

        val clubCategory = intent.getIntExtra(CATEGORY_ID, -1)
        val clubId = intent.getIntExtra(CLUB_ID, -1)
        val extraClubId = intent.getIntExtra(EXTRA_CLUB_ID, -1)
        val extraEventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)

        val startDestination: ClubNavType = when {
            extraClubId != -1 -> if (extraEventId != -1) {
                ClubNavType.ClubDetail(clubId = extraClubId, eventId = extraEventId)
            } else {
                ClubNavType.ClubDetail(clubId = extraClubId, recruitEvent = true)
            }

            clubId != -1 -> ClubNavType.ClubDetail(clubId = clubId)
            else -> ClubNavType.ClubList(categoryId = clubCategory)
        }

        setContent {
            KoinTheme {
                val navController = rememberNavController()

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
