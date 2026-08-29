package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController,
    onNavigateToLogin: () -> Unit = {}
) {
    composable<RecruitmentNavType.ApplicantManagement> {
        ApplicantManagementScreen()
    }
    composable<RecruitmentNavType.MyRecruitment> {
        MyRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToLogin = onNavigateToLogin
        )
    }
}
