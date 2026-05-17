package `in`.koreatech.koin.feature.club.navigation

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
    composable<ClubNavType.ClubList> { entry ->
        val isClubCreated by entry.savedStateHandle.getStateFlow(IS_CLUB_CREATED, initialValue = false)
            .collectAsStateWithLifecycle()

        ClubListScreen(
            isClubCreated = isClubCreated,
            navigateToCreateClub = {
                navController.navigate(ClubNavType.ClubCreate)
            },
            navigateToClubDetail = { clubId ->
                navController.navigate(ClubNavType.ClubDetail(clubId = clubId))
            },
            resetClubCreatedState = {
                entry.savedStateHandle[IS_CLUB_CREATED] = false
            }
        )
    }

    composable<ClubNavType.ClubDetail> { entry ->
        val args = entry.toRoute<ClubNavType.ClubDetail>()
        val isClubModified by entry.savedStateHandle.getStateFlow(IS_CLUB_MODIFIED, initialValue = false).collectAsStateWithLifecycle()

        val context = LocalContext.current

        ClubDetail(
            isClubModified = isClubModified,
            isRecruitEvent = args.recruitEvent,
            norificationEventId = args.eventId,
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onModifyClick = { clubId ->
                navController.navigate(ClubNavType.ClubModify(clubId = clubId))
            },
            onRecruitCreateClick = { clubId ->
                navController.navigate(ClubNavType.ClubRecruitCreate(clubId = clubId))
            },
            onRecruitModifyClick = { clubId ->
                navController.navigate(ClubNavType.ClubRecruitModify(clubId = clubId))
            },
            onEventCreateClick = { clubId ->
                navController.navigate(ClubNavType.ClubEventCreate(clubId = clubId))
            },
            onEventModifyClick = { clubId, eventId ->
                navController.navigate(ClubNavType.ClubEventModify(clubId = clubId, eventId = eventId))
            },
            resetClubModifiedState = {
                entry.savedStateHandle[IS_CLUB_MODIFIED] = false
            },
            resetNorificationEventId = {
                // No-op: eventId is part of args, no separate state mutation needed
            }
        )
    }

    composable<ClubNavType.ClubCreate> {
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

    composable<ClubNavType.ClubModify> {
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

    composable<ClubNavType.ClubRecruitCreate> {
        ClubRecruitCreateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onRecruitCreated = {
                navController.navigateUp()
            }
        )
    }

    composable<ClubNavType.ClubRecruitModify> {
        ClubRecruitModifyScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onRecruitModified = {
                navController.navigateUp()
            }
        )
    }

    composable<ClubNavType.ClubEventCreate> {
        ClubEventCreateScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onEventCreated = {
                navController.navigateUp()
            }
        )
    }

    composable<ClubNavType.ClubEventModify> {
        ClubEventModifyScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            onEventModified = {
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
const val RECRUIT_EVENT = "recruitEvent"
const val EXTRA_CLUB_ID = "extra_club_id"
const val EXTRA_EVENT_ID = "extra_event_id"
