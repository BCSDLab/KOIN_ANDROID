package `in`.koreatech.koin.feature.user.ui.findid.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.user.ui.findid.complete.FindIdComplete
import `in`.koreatech.koin.feature.user.ui.findid.verification.FindIdVerification

fun NavGraphBuilder.koinFindIdNavigation(
    navController: NavController
) {
    composable<FindIdNavType.Verification> {
        FindIdVerification {
            navController.navigate(FindIdNavType.Complete(loginId = it))
        }
    }

    composable<FindIdNavType.Complete> { entry ->
        val args = entry.toRoute<FindIdNavType.Complete>()
        FindIdComplete(args.loginId)
    }
}

const val LOGIN_ID = "loginId"
