package `in`.koreatech.koin.feature.club.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.club.ui.clublist.ClubListScreen
import `in`.koreatech.koin.feature.club.ui.detail.ClubDetail

fun NavGraphBuilder.koinClubGraph(
    navController: NavController
) {
    composable(
        route = "${ClubNavType.ClubList.route}/{$CATEGORY_ID}",
        arguments = listOf(
            navArgument(CATEGORY_ID) { type = NavType.IntType }
        )
    ) {
        ClubListScreen(
            navigateToClubDetail = { clubId ->
                navController.navigate("${ClubNavType.ClubDetail.route}/$clubId")
            }
        )
    }

    composable(
        route = "${ClubNavType.ClubDetail.route}/{$CLUB_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType }
        )
    ) {
        ClubDetail(
            onTopbarBackClick = { navController.popBackStack() }
        )
    }

    composable(
        route = ClubNavType.ClubCreate.route
    ) {
    }

    composable(
        route = ClubNavType.ClubModify.route
    ) {
    }
}

const val CLUB_ID = "clubId"
const val CATEGORY_ID = "categoryId"
