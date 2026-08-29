package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.ApplicantDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.directchat.RecruitmentDirectChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat.RecruitmentGroupChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.detail.RecruitmentDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.main.RecruitmentMainScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.notification.RecruitmentNotificationScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentMain> {
        val context = LocalContext.current
        RecruitmentMainScreen(
            onTopbarBackClick = { (context as? ComponentActivity)?.finish() }
        )
    }
    composable<RecruitmentNavType.RecruitmentDetail> {
        val context = LocalContext.current
        RecruitmentDetailScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) (context as? ComponentActivity)?.finish()
            }
        )
    }
    composable<RecruitmentNavType.RecruitmentGroupChat> {
        RecruitmentGroupChatScreen()
    }
    composable<RecruitmentNavType.RecruitmentDirectChat> {
        RecruitmentDirectChatScreen()
    }
    composable<RecruitmentNavType.Notification> {
        RecruitmentNotificationScreen(
            onBack = { navController.popBackStack() },
            onNavigateToPost = {}
        )
    }
    composable<RecruitmentNavType.ApplicantManagement> { backStackEntry ->
        val route = backStackEntry.toRoute<RecruitmentNavType.ApplicantManagement>()
        ApplicantManagementScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplicantDetail = { applicantId ->
                navController.navigate(RecruitmentNavType.ApplicantDetail(route.postId, applicantId))
            }
        )
    }
    composable<RecruitmentNavType.MyRecruitment> {
        MyRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.ApplicantDetail> {
        ApplicantDetailScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
}
