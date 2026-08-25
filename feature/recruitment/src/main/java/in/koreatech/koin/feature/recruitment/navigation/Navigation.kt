package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.MyAppliedRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen

@Suppress("EmptyFunctionBlock")
fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.MyAppliedRecruitment> {
        MyAppliedRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.MyRecruitment> {
        MyRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
}
