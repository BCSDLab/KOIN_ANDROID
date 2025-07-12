package `in`.koreatech.koin.feature.club.navigation

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
import `in`.koreatech.koin.feature.club.ui.clubrecruitcreate.ClubRecruitCreateScreen

fun NavGraphBuilder.koinClubGraph(
    navController: NavController
) {
    composable(
        route = "${ClubNavType.ClubList.route}/{$CATEGORY_ID}",
        arguments = listOf(
            navArgument(CATEGORY_ID) { type = NavType.IntType }
        )
    ) {
        val isClubCreated by it.savedStateHandle.getStateFlow(IS_CLUB_CREATED, initialValue = false)
            .collectAsStateWithLifecycle()

        ClubListScreen(
            isClubCreated = isClubCreated,
            navigateToCreateClub = {
                navController.navigate(ClubNavType.ClubCreate.route)
            },
            navigateToClubDetail = { clubId ->
                navController.navigate("${ClubNavType.ClubDetail.route}/$clubId")
            },
            resetClubCreatedState = {
                it.savedStateHandle[IS_CLUB_CREATED] = false
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
        val context = LocalContext.current

        ClubDetail(
            isClubModified = isClubModified,
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onModifyClick = { clubId ->
                navController.navigate("${ClubNavType.ClubModify.route}/$clubId")
            },
            onRecruitCreateClick = { clubId ->
                navController.navigate("${ClubNavType.ClubRecruitCreate.route}/$clubId")
            },
            resetClubModifiedState = {
                it.savedStateHandle[IS_CLUB_MODIFIED] = false
            }
        )
    }

    composable(
        route = ClubNavType.ClubCreate.route
    ) {
        ClubCreateScreen(
            onNavigateUp = {
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

    composable(
        route = "${ClubNavType.ClubRecruitCreate.route}/{$CLUB_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType }
        )
    ) {
        ClubRecruitCreateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onRecruitCreated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_RECRUIT_CREATED,
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
const val IS_CLUB_RECRUIT_CREATED = "isClubRecruitCreated"
