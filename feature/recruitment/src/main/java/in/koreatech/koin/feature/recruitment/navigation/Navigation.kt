package `in`.koreatech.koin.feature.recruitment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.recruitment.ui.profile.ProfileScreen
import `in`.koreatech.koin.feature.recruitment.ui.profilecreate.ProfileCreateScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.RecruitmentApplyScreen
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.RecruitmentCreateScreen

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
        )
    }
}
