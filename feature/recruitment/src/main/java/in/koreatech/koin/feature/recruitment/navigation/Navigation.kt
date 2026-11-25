package `in`.koreatech.koin.feature.recruitment.navigation

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.ApplicantDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.ApplicantManagementScreen
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
            onRecruitmentCreated = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_RECRUITMENT_CREATED,
                    true
                )
                navController.navigateUp()
            }
        )
    }
    composable<RecruitmentNavType.RecruitmentApply>(
        typeMap = mapOf(
            typeOf<List<RecruitmentRoleArg>>() to RecruitmentRoleArgListNavType
        )
    ) {
        RecruitmentApplyScreen(
            onNavigateUp = { navController.navigateUp() },
            onApplySuccess = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_RECRUITMENT_REFRESH_NEEDED,
                    true
                )
                navController.navigateUp()
            }
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
    composable<RecruitmentNavType.RecruitmentMain> { entry ->
        val isRecruitmentCreated by entry.savedStateHandle
            .getStateFlow(IS_RECRUITMENT_CREATED, initialValue = false)
            .collectAsStateWithLifecycle()
        val isRecruitmentModified by entry.savedStateHandle
            .getStateFlow(IS_RECRUITMENT_MODIFIED, initialValue = false)
            .collectAsStateWithLifecycle()
        val context = LocalContext.current
        val navigator = rememberNavigator()

        RecruitmentMainScreen(
            isRecruitmentCreated = isRecruitmentCreated,
            onResetRecruitmentCreated = { entry.savedStateHandle[IS_RECRUITMENT_CREATED] = false },
            isRecruitmentModified = isRecruitmentModified,
            onResetRecruitmentModified = { entry.savedStateHandle[IS_RECRUITMENT_MODIFIED] = false },
            navController = navController,
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onNotificationClick = { navController.navigate(RecruitmentNavType.Notification) },
            onProfileClick = { navController.navigate(RecruitmentNavType.Profile) },
            onWriteClick = {
                navController.navigate(RecruitmentNavType.RecruitmentCreate) {
                    launchSingleTop = true
                }
            },
            onNavigateToLogin = {
                navigator.navigateToSignIn(context).apply {
                    context.startActivity(this)
                }
            },
            onItemClick = { postId ->
                navController.navigate(RecruitmentNavType.RecruitmentDetail(postId))
            }
        )
    }
    composable<RecruitmentNavType.RecruitmentDetail> { entry ->
        val isRecruitmentRefreshNeeded by entry.savedStateHandle
            .getStateFlow(IS_RECRUITMENT_REFRESH_NEEDED, initialValue = false)
            .collectAsStateWithLifecycle()
        val navigator = rememberNavigator()
        val context = LocalContext.current

        RecruitmentDetailScreen(
            isModified = isRecruitmentRefreshNeeded,
            onResetModified = { entry.savedStateHandle[IS_RECRUITMENT_REFRESH_NEEDED] = false },
            onTopbarBackClick = { navController.navigateUp() },
            onNavigateToModify = { postId ->
                navController.navigate(RecruitmentNavType.RecruitmentModify(postId))
            },
            onNavigateToLogin = {
                navigator.navigateToSignIn(context).apply {
                    context.startActivity(this)
                }
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
    composable<RecruitmentNavType.Notification> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        RecruitmentNotificationScreen(
            onBack = { navController.popBackStack() },
            onNavigateToApplicantManagement = { recruitmentId ->
                navController.navigate(RecruitmentNavType.ApplicantManagement(recruitmentId))
            },
            onNavigateToGroupChat = { recruitmentId, chatRoomId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = recruitmentId,
                    chatRoomId = chatRoomId
                ).apply {
                    context.startActivity(this)
                }
            },
            onNavigateToDirectChat = { recruitmentId, applicationId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = recruitmentId,
                    applicationId = applicationId
                ).apply {
                    context.startActivity(this)
                }
            },
            onNavigateToMyAppliedRecruitment = {
                navController.navigate(RecruitmentNavType.MyAppliedRecruitment)
            },
            onUnreadNotificationStateChanged = {
                navController.previousBackStackEntry?.savedStateHandle?.set(UNREAD_NOTIFICATION_STATE_UPDATE, true)
            }
        )
    }
    composable<RecruitmentNavType.ApplicantManagement> { backStackEntry ->
        val route = backStackEntry.toRoute<RecruitmentNavType.ApplicantManagement>()
        val navigator = rememberNavigator()
        val context = LocalContext.current
        ApplicantManagementScreen(
            navController = navController,
            onNavigateUp = { navController.navigateUp() },
            onChat = { chatRoomId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = route.postId,
                    chatRoomId = chatRoomId
                ).apply {
                    context.startActivity(this)
                }
            },
            onApplicantDetail = { applicantId ->
                navController.navigate(RecruitmentNavType.ApplicantDetail(route.postId, applicantId))
            },
            onApplicantChat = { applicantId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = route.postId,
                    applicationId = applicantId
                ).apply {
                    context.startActivity(this)
                }
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
            },
            onNavigateToMain = {
                navController.popBackStack(RecruitmentNavType.RecruitmentMain, inclusive = false)
            },
            onNavigateToGroupChat = { recruitmentId, chatRoomId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = recruitmentId,
                    chatRoomId = chatRoomId
                ).apply {
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
            },
            onChat = { recruitmentId, chatRoomId ->
                navigator.navigateToChatRoom(
                    context = context,
                    domainType = ChatDomain.TEAM_RECRUITMENT,
                    contextId = recruitmentId,
                    chatRoomId = chatRoomId
                ).apply {
                    context.startActivity(this)
                }
            }
        )
    }
    composable<RecruitmentNavType.ApplicantDetail> {
        ApplicantDetailScreen(
            navController = navController,
            onNavigateUp = { navController.navigateUp() }
        )
    }
    composable<RecruitmentNavType.RecruitmentModify> {
        RecruitmentModifyScreen(
            onNavigateUp = { navController.navigateUp() },
            onRecruitmentModified = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IS_RECRUITMENT_REFRESH_NEEDED,
                    true
                )
                navController.getBackStackEntry(RecruitmentNavType.RecruitmentMain)
                    .savedStateHandle[IS_RECRUITMENT_MODIFIED] = true
                navController.navigateUp()
            }
        )
    }
}

const val UNREAD_NOTIFICATION_STATE_UPDATE = "unreadNotificationStateUpdate"
const val APPLICANT_STATE_UPDATE = "applicantStateUpdate"

private const val IS_RECRUITMENT_MODIFIED = "isRecruitmentModified"
private const val IS_RECRUITMENT_REFRESH_NEEDED = "isRecruitmentRefreshNeeded"
private const val IS_RECRUITMENT_CREATED = "isRecruitmentCreated"
