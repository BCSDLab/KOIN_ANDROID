package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.RecruitmentCreateScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentCreate> {
        RecruitmentCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onRecruitmentCreated = { navController.navigateUp() }
        )
    }
}
