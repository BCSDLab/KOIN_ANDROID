package `in`.koreatech.koin.feature.club.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.club.ui.clubcreate.ClubCreateScreen
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
        val isClubCreated by it.savedStateHandle.getStateFlow(IS_CLUB_CREATED, initialValue = false).collectAsStateWithLifecycle()

        ClubListScreen(
            isClubCreated = isClubCreated,
            navigateToCreateClub = {
                navController.navigate(ClubNavType.ClubCreate.route)
            }
        )
    }

    composable(
        route = ClubNavType.ClubDetail.route
    ) {
        ClubDetail(
            onTopbarBackClick = { navController.popBackStack() }
        )
    }

    composable(
        route = ClubNavType.ClubCreate.route
    ) {
        ClubCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onClubCreated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_CREATED,
                    true
                )
                navController.navigateUp()
            }
        )
    }

    composable(
        route = ClubNavType.ClubModify.route
    ) {
    }
}

const val CATEGORY_ID = "categoryId"
const val IS_CLUB_CREATED = "isClubCreated"