package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.ApplicantDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.directchat.RecruitmentDirectChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat.RecruitmentGroupChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.detail.RecruitmentDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.main.RecruitmentMainScreen
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.MyAppliedRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.notification.RecruitmentNotificationScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.RecruitmentApplyScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.RecruitmentCreateScreen

@Suppress("LongMethod")
fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentCreate> {
        RecruitmentCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onRecruitmentCreated = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.RecruitmentMain> {
        RecruitmentMainScreen(
            onTopbarBackClick = { navController.navigateUp() },
            onItemClick = { postId ->
                navController.navigate(RecruitmentNavType.RecruitmentDetail(postId))
            }
        )
    }
    composable<RecruitmentNavType.RecruitmentDetail> {
        RecruitmentDetailScreen(
            onTopbarBackClick = { navController.navigateUp() }
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
            onNavigateToApplicantManagement = { recruitmentId ->
                navController.navigate(RecruitmentNavType.ApplicantManagement(recruitmentId))
            }
            // TODO: CHAT_ROOM / MY_APPLICATIONS / 모집글 상세 라우트가 추가되면 콜백을 추가 연결한다.
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
    composable<RecruitmentNavType.MyAppliedRecruitment> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        MyAppliedRecruitmentScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToLogin = {
                navigator.navigateToSignIn(context).apply {
                    context.startActivity(this)
                }
            }
        )
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
    composable<RecruitmentNavType.ApplicantDetail> {
        ApplicantDetailScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }

    composable<RecruitmentNavType.RecruitmentApply> {
        RecruitmentApplyScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplySuccess = { navController.navigateUp() }
        )
    }
}
