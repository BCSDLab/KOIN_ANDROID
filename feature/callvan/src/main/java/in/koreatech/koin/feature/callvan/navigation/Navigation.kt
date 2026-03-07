package `in`.koreatech.koin.feature.callvan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.callvan.ui.detail.CallvanDetailScreen
import `in`.koreatech.koin.feature.callvan.ui.notification.CallvanNotificationsScreen

fun NavGraphBuilder.koinCallvanGraph(
    navController: NavController
) {
    composable<CallvanNavType.CallvanMain> {
    }

    composable<CallvanNavType.CallvanDetail> { backStackEntry ->
        val postId = backStackEntry.toRoute<CallvanNavType.CallvanDetail>().postId
        CallvanDetailScreen(
            onTopbarBackClick = { navController.popBackStack() },
            onNotificationClick = { navController.navigate(CallvanNavType.CallvanNotifications) },
            onEnterChatClick = { navController.navigate(CallvanNavType.CallvanChat(postId)) },
            onReportClick = { navController.navigate(CallvanNavType.CallvanReport(postId)) }
        )
    }

    composable<CallvanNavType.CallvanCreate> {
    }

    composable<CallvanNavType.CallvanChat> {
    }

    composable<CallvanNavType.CallvanNotifications> {
        CallvanNotificationsScreen(
            onTopbarBackClick = { navController.popBackStack() }
        )
    }

    composable<CallvanNavType.CallvanReport> {
    }
}
