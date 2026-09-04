package `in`.koreatech.koin.feature.recruitment.navigation

import android.app.Activity
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
import `in`.koreatech.koin.feature.recruitment.ui.profile.ProfileScreen
import `in`.koreatech.koin.feature.recruitment.ui.profilecreate.ProfileCreateScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.RecruitmentApplyScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.RecruitmentCreateScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.RecruitmentModifyScreen
import kotlin.reflect.typeOf

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
    composable<RecruitmentNavType.RecruitmentApply>(
        typeMap = mapOf(
            typeOf<List<RecruitmentRoleArg>>() to RecruitmentRoleArgListNavType
        )
    ) {
        RecruitmentApplyScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplySuccess = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.Profile> {
        ProfileScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToMyRecruitment = { navController.navigate(RecruitmentNavType.MyRecruitment) },
            onNavigateToMyAppliedRecruitment = { navController.navigate(RecruitmentNavType.MyAppliedRecruitment) },
            onNavigateToProfileCreate = { isEditMode ->
                navController.navigate(RecruitmentNavType.ProfileCreate(isEditMode = isEditMode))
            }
        )
    }
    composable<RecruitmentNavType.ProfileCreate> {
        ProfileCreateScreen(
            onNavigateUp = { navController.navigateUp() },
            onSaveSuccess = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.RecruitmentMain> {
        val context = LocalContext.current

        RecruitmentMainScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onNotificationClick = { navController.navigate(RecruitmentNavType.Notification) },
            onProfileClick = { navController.navigate(RecruitmentNavType.Profile) },
            onWriteClick = { navController.navigate(RecruitmentNavType.RecruitmentCreate) },
            onItemClick = { postId ->
                navController.navigate(RecruitmentNavType.RecruitmentDetail(postId))
            }
        )
    }
    composable<RecruitmentNavType.RecruitmentDetail> {
        RecruitmentDetailScreen(
            onTopbarBackClick = { navController.navigateUp() },
            onNavigateToModify = { postId ->
                navController.navigate(RecruitmentNavType.RecruitmentModify(postId))
            },
            onNavigateToApply = { postId, roles ->
                navController.navigate(
                    RecruitmentNavType.RecruitmentApply(
                        recruitmentId = postId,
                        roles = roles.map { role ->
                            RecruitmentRoleArg(id = role.id, name = role.name, isClosed = role.isClosed)
                        }
                    )
                )
            },
            onNavigateToApplicantManagement = { postId ->
                navController.navigate(RecruitmentNavType.ApplicantManagement(postId))
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
            onChat = { chatRoomId ->
                navController.navigate(
                    RecruitmentNavType.RecruitmentGroupChat(
                        recruitmentId = route.postId,
                        chatRoomId = chatRoomId
                    )
                )
            },
            onApplicantDetail = { applicantId ->
                navController.navigate(RecruitmentNavType.ApplicantDetail(route.postId, applicantId))
            },
            onApplicantChat = { applicantId ->
                navController.navigate(
                    RecruitmentNavType.RecruitmentDirectChat(recruitmentId = route.postId, applicationId = applicantId)
                )
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
            },
            onApplicantManage = { postId ->
                navController.navigate(RecruitmentNavType.ApplicantManagement(postId))
            }
        )
    }
    composable<RecruitmentNavType.ApplicantDetail> {
        ApplicantDetailScreen(
            onNavigateUp = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.RecruitmentModify> {
        RecruitmentModifyScreen(
            onNavigateUp = { navController.navigateUp() },
            onRecruitmentModified = { navController.navigateUp() }
        )
    }
}
