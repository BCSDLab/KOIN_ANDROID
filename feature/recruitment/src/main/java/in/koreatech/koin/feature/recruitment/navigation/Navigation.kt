package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.profile.ProfileScreen
import `in`.koreatech.koin.feature.recruitment.ui.profilecreate.ProfileCreateScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.RecruitmentApplyScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.RecruitmentCreateScreen
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.ApplicantDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.directchat.RecruitmentDirectChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat.RecruitmentGroupChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.MyRecruitmentScreen
import `in`.koreatech.koin.feature.recruitment.ui.notification.RecruitmentNotificationScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentCreate> {
        RecruitmentCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onRecruitmentCreated = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.RecruitmentApply> {
        RecruitmentApplyScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplySuccess = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.Profile> {
        ProfileScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToMyRecruitment = { },
            onNavigateToMyAppliedRecruitment = { },
            onNavigateToProfileCreate = { isEditMode ->
                navController.navigate(RecruitmentNavType.ProfileCreate(isEditMode = isEditMode))
            }
        )
    }
    composable<RecruitmentNavType.ProfileCreate> {
        ProfileCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onSaveSuccess = { navController.navigateUp() }
            onNavigateToMyAppliedRecruitment = { }
    composable<RecruitmentNavType.RecruitmentGroupChat> {
        RecruitmentGroupChatScreen()
    }
    composable<RecruitmentNavType.RecruitmentDirectChat> {
        RecruitmentDirectChatScreen()
    }
    composable<RecruitmentNavType.Notification> {
        RecruitmentNotificationScreen(
            onBack = { navController.popBackStack() },
            onNavigateToPost = {
                // TODO: 모집글 상세 화면 라우트가 추가되면 postId 로 이동하도록 연결한다.
            }
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
