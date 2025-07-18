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
import `in`.koreatech.koin.feature.club.ui.clubeventcreate.ClubEventCreateScreen
import `in`.koreatech.koin.feature.club.ui.clubeventmodify.ClubEventModifyScreen
import `in`.koreatech.koin.feature.club.ui.clublist.ClubListScreen
import `in`.koreatech.koin.feature.club.ui.clubmodify.ClubModifyScreen
import `in`.koreatech.koin.feature.club.ui.clubrecruitcreate.ClubRecruitCreateScreen
import `in`.koreatech.koin.feature.club.ui.clubrecruitmodify.ClubRecruitModifyScreen

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
            onRecruitModifyClick = { clubId ->
                navController.navigate("${ClubNavType.ClubRecruitModify.route}/$clubId")
            },
            onEventCreateClick = { clubId ->
                navController.navigate("${ClubNavType.ClubEventCreate.route}/$clubId")
            },
            onEventModifyClick = { clubId, eventId ->
                navController.navigate("${ClubNavType.ClubEventModify.route}/$clubId/$eventId")
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

    composable(
        route = "${ClubNavType.ClubRecruitModify.route}/{$CLUB_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType }
        )
    ) {
        ClubRecruitModifyScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onRecruitModified = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_RECRUIT_MODIFIED,
                    true
                )
                navController.navigateUp()
            }
        )
    }

    composable(
        route = "${ClubNavType.ClubEventCreate.route}/{$CLUB_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType }
        )
    ) {
        ClubEventCreateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onEventCreated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_EVENT_CREATED,
                    true
                )
                navController.navigateUp()
            }
        )
    }

    composable(
        route = "${ClubNavType.ClubEventModify.route}/{$CLUB_ID}/{$EVENT_ID}",
        arguments = listOf(
            navArgument(CLUB_ID) { type = NavType.IntType },
            navArgument(EVENT_ID) { type = NavType.IntType }
        )
    ) {
        ClubEventModifyScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onEventModified = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_CLUB_RECRUIT_MODIFIED,
                    true
                )
                navController.navigateUp()
            }
        )
    }
}

const val CLUB_ID = "clubId"
const val EVENT_ID = "eventId"
const val CATEGORY_ID = "categoryId"
const val IS_CLUB_CREATED = "isClubCreated"
const val IS_CLUB_MODIFIED = "isClubModified"
const val IS_CLUB_RECRUIT_CREATED = "isClubRecruitCreated"
const val IS_CLUB_RECRUIT_MODIFIED = "isClubRecruitModified"
const val IS_CLUB_EVENT_CREATED = "isClubRecruitModified"
const val IS_CLUB_EVENT_MODIFIED = "isClubRecruitModified"
