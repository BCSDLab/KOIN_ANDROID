package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.chat.directchat.RecruitmentDirectChatScreen
import `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat.RecruitmentGroupChatScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentGroupChat> {
        RecruitmentGroupChatScreen()
    }

    composable<RecruitmentNavType.RecruitmentDirectChat> {
        RecruitmentDirectChatScreen()
    }
}
