package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.ApplicantManagement> {
        ApplicantManagementScreen()
    }
    composable<RecruitmentNavType.MyRecruitment> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        MyRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToLogin = {
                navigator.navigateToSignIn(context).apply {
                    context.startActivity(this)
                }
            }
        )
    }
}
