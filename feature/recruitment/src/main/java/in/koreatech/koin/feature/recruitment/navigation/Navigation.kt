package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.MyAppliedRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.MyRecruitmentList> {
        MyRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.MyAppliedRecruitmentList> {
        MyAppliedRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
}
