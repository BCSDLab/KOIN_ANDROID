package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.ApplicantDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.ApplicantManagement> { backStackEntry ->
        val route = backStackEntry.toRoute<RecruitmentNavType.ApplicantManagement>()
        ApplicantManagementScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplicantDetail = { applicantId ->
                navController.navigate(RecruitmentNavType.ApplicantDetail(route.postId, applicantId))
            }
        )
    }
    composable<RecruitmentNavType.ApplicantDetail> {
        ApplicantDetailScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
}
