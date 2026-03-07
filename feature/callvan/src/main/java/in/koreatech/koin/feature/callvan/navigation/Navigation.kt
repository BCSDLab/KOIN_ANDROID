package `in`.koreatech.koin.feature.callvan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.callvan.ui.create.CallvanCreateScreen
import `in`.koreatech.koin.feature.callvan.ui.notification.CallvanNotificationsScreen

fun NavGraphBuilder.koinCallvanGraph(
    navController: NavController
) {
    composable<CallvanNavType.CallvanMain> {
    }

    composable<CallvanNavType.CallvanDetail> {
    }

    composable<CallvanNavType.CallvanCreate> {
        CallvanCreateScreen(
            onNavigateToDetail = { postId ->
                navController.navigate(CallvanNavType.CallvanDetail(postId))
            },
            onTopbarBackClick = { navController.popBackStack() }
        )
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
