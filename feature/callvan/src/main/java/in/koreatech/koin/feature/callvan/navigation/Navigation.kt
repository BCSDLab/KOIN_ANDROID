package `in`.koreatech.koin.feature.callvan.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.callvan.ui.detail.CallvanDetailScreen
import `in`.koreatech.koin.feature.callvan.ui.list.CallvanListScreen
import `in`.koreatech.koin.feature.callvan.ui.detail.CallvanDetailViewModel
import `in`.koreatech.koin.feature.callvan.ui.notification.CallvanNotificationsScreen
import `in`.koreatech.koin.feature.callvan.ui.report.CallvanReportScreen

private const val STORE_CATEGORY = "STORE_CATEGORY"
private const val STORE_CATEGORY_CALLVAN_PRODUCTION = 10

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
            onLoginClick = { context.startActivity(navigator.navigateToSignIn(context)) },
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
        CallvanDetailScreen(
            onTopbarBackClick = { navController.popBackStackOrFinish(context) },
            onNotificationClick = { navController.navigate(CallvanNavType.CallvanNotifications) },
            onEnterChatClick = { navController.navigate(CallvanNavType.CallvanChat(postId)) },
            onReportClick = { reportedUserId ->
                navController.navigate(CallvanNavType.CallvanReport(postId, reportedUserId))
            }
        )
    }

    composable<CallvanNavType.CallvanCreate> {
    }

    composable<CallvanNavType.CallvanChat> {
        val navigator = rememberNavigator()
        val context = LocalContext.current
        val intent = remember { navigator.navigateToGroupChat(context, it.toRoute<CallvanNavType.CallvanChat>().postId) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            navController.popBackStack()
        }
        LaunchedEffect(Unit) {
            launcher.launch(intent)
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

private fun NavController.popBackStackOrFinish(context: Context) {
    if (!popBackStack()) (context as? ComponentActivity)?.finish()
}

