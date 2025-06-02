package `in`.koreatech.koin.feature.club.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.club.ui.clubcreate.ClubCreateScreen
import `in`.koreatech.koin.feature.club.ui.clubdetail.ClubDetail
import `in`.koreatech.koin.feature.club.ui.clublist.ClubListScreen
import `in`.koreatech.koin.feature.club.ui.clubmodify.ClubModifyScreen

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
            },
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
        val isClubModified by it.savedStateHandle.getStateFlow(IS_CLUB_MODIFIED, initialValue = false).collectAsStateWithLifecycle()

        ClubDetail(
            isClubModified = isClubModified,
            onTopbarBackClick = { navController.popBackStack() },
            onModifyClick = { clubId ->
                navController.navigate("${ClubNavType.ClubModify.route}/$clubId")
            },
        )
    }

    composable(
        route = ClubNavType.ClubCreate.route
    ) {
        ClubCreateScreen(
            onNavigateUp = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_CREATED,
                    false
                )
                navController.navigateUp()
            },
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
        route = "${ClubNavType.ClubModify.route}/{$CLUB_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType }
        )
    ) {
        ClubModifyScreen(
            onNavigateUp = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_MODIFIED,
                    false
                )
                navController.navigateUp()
            },
            onClubModified = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_MODIFIED,
                    true
                )
                navController.navigateUp()
            }
        )
    }
}

const val CLUB_ID = "clubId"
const val CATEGORY_ID = "categoryId"
const val IS_CLUB_CREATED = "isClubCreated"
const val IS_CLUB_MODIFIED = "isClubModified"
