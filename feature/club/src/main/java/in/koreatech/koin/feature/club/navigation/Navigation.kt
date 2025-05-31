package `in`.koreatech.koin.feature.club.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.club.ui.clubcreate.ClubCreateScreen
import `in`.koreatech.koin.feature.club.ui.clublist.ClubListScreen

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
            navigateToCreateClub = {
                navController.navigate(ClubNavType.ClubCreate.route)
            }
        )
    }

    composable(
        route = ClubNavType.ClubDetail.route
    ) {
    }

    composable(
        route = ClubNavType.ClubCreate.route
    ) {
        ClubCreateScreen(
            onBackPressed = { navController.navigateUp() }
        )
    }

    composable(
        route = ClubNavType.ClubModify.route
    ) {
    }
}

const val CATEGORY_ID = "categoryId"
