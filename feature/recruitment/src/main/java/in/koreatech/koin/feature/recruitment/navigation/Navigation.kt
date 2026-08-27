package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.notification.RecruitmentNotificationScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.Notification> {
        RecruitmentNotificationScreen(
            onBack = { navController.popBackStack() },
            onNavigateToPost = {
                // TODO: 모집글 상세 화면 라우트가 추가되면 postId 로 이동하도록 연결한다.
            }
        )
    }
}
