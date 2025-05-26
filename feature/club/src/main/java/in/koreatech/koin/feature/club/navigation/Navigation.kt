package `in`.koreatech.koin.feature.club.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.club.ui.detail.ClubDetail

fun NavGraphBuilder.koinClubGraph(
    navController: NavController
) {
    composable(
        route = ClubNavType.ClubList.route
    ) {
    }

    composable(
        route = ClubNavType.ClubDetail.route
    ) {
        ClubDetail()
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
