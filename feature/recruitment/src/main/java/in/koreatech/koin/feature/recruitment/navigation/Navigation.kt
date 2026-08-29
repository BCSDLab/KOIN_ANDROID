package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.detail.RecruitmentDetailScreen
import `in`.koreatech.koin.feature.recruitment.ui.main.RecruitmentMainScreen

fun NavGraphBuilder.koinRecruitmentGraph(
    navController: NavController
) {
    composable<RecruitmentNavType.RecruitmentMain> {
        val context = LocalContext.current
        RecruitmentMainScreen(
            onTopbarBackClick = { (context as? ComponentActivity)?.finish() }
        )
    }
    composable<RecruitmentNavType.RecruitmentDetail> {
        val context = LocalContext.current
        RecruitmentDetailScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) (context as? ComponentActivity)?.finish()
            }
        )
    }
}
