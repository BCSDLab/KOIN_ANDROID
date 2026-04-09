package `in`.koreatech.koin.feature.callvan.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.EXTRA_POST_ID
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.callvan.DEEPLINK_CALLVAN
import `in`.koreatech.koin.feature.callvan.ui.create.CallvanCreateScreen
import `in`.koreatech.koin.feature.callvan.ui.detail.CallvanDetailScreen
import `in`.koreatech.koin.feature.callvan.ui.detail.CallvanDetailViewModel
import `in`.koreatech.koin.feature.callvan.ui.list.CallvanListScreen
import `in`.koreatech.koin.feature.callvan.ui.notification.CallvanNotificationsScreen
import `in`.koreatech.koin.feature.callvan.ui.report.CallvanReportScreen
import `in`.koreatech.koin.feature.callvan.util.popBackStackOrFinish

private const val STORE_CATEGORY = "STORE_CATEGORY"
private const val STORE_CATEGORY_CALLVAN_PRODUCTION = 10

@Suppress("LongMethod")
fun NavGraphBuilder.koinCallvanGraph(
    navController: NavController
) {
    composable<CallvanNavType.CallvanMain> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        CallvanListScreen(
            onTopbarBackClick = { navController.popBackStackOrFinish(context) },
            onNotificationClick = { navController.navigate(CallvanNavType.CallvanNotifications) },
            onWriteClick = { navController.navigate(CallvanNavType.CallvanCreate) },
            onLoginClick = { context.startActivity(navigator.navigateToSignIn(context, DEEPLINK_CALLVAN)) },
            onChatClick = { postId -> navController.navigate(CallvanNavType.CallvanChat(postId)) },
            onCallClick = {
                context.startActivity(
                    navigator.navigateToStore(context).apply {
                        putExtra(STORE_CATEGORY, STORE_CATEGORY_CALLVAN_PRODUCTION)
                    }
                )
            },
            onDetailClick = { postId -> navController.navigate(CallvanNavType.CallvanDetail(postId)) }
        )
    }

    composable<CallvanNavType.CallvanDetail> { backStackEntry ->
        val postId = backStackEntry.toRoute<CallvanNavType.CallvanDetail>().postId
        val context = LocalContext.current
        val reportSuccess = backStackEntry.savedStateHandle
            .getStateFlow(CallvanDetailViewModel.KEY_REPORT_SUCCESS, false)
            .collectAsState()

        CallvanDetailScreen(
            reportSuccess = reportSuccess.value,
            onReportSuccessShown = {
                backStackEntry.savedStateHandle[CallvanDetailViewModel.KEY_REPORT_SUCCESS] = false
            },
            onTopbarBackClick = { navController.popBackStackOrFinish(context) },
            onNotificationClick = { navController.navigate(CallvanNavType.CallvanNotifications) },
            onEnterChatClick = { navController.navigate(CallvanNavType.CallvanChat(postId)) },
            onReportClick = { reportedUserId ->
                navController.navigate(CallvanNavType.CallvanReport(postId, reportedUserId))
            }
        )
    }

    composable<CallvanNavType.CallvanCreate> {
        val context = LocalContext.current
        CallvanCreateScreen(
            onCompleteAndNavigateToMain = {
                navController.navigate(CallvanNavType.CallvanMain) {
                    popUpTo(CallvanNavType.CallvanMain::class) { inclusive = true }
                }
            },
            onTopbarBackClick = { navController.popBackStackOrFinish(context) }
        )
    }

    composable<CallvanNavType.CallvanChat> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            navController.popBackStack()
        }
        LaunchedEffect(Unit) {
            navigator.navigateToGroupChat(context).apply {
                putExtra(EXTRA_POST_ID, it.toRoute<CallvanNavType.CallvanChat>().postId)
            }.let(launcher::launch)
        }
    }

    composable<CallvanNavType.CallvanNotifications> {
        val context = LocalContext.current
        CallvanNotificationsScreen(
            onTopbarBackClick = { navController.popBackStackOrFinish(context) }
        )
    }

    composable<CallvanNavType.CallvanReport> {
        val context = LocalContext.current
        CallvanReportScreen(
            onTopbarBackClick = { navController.popBackStackOrFinish(context) },
            onSubmitSuccess = {
                navController.previousBackStackEntry?.savedStateHandle?.set(CallvanDetailViewModel.KEY_REPORT_SUCCESS, true)
                navController.popBackStack()
            }
        )
    }
}
