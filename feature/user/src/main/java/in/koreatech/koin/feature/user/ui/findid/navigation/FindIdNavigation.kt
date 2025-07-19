package `in`.koreatech.koin.feature.user.ui.findid.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.user.ui.findid.complete.FindIdComplete
import `in`.koreatech.koin.feature.user.ui.findid.verification.FindIdVerification

fun NavGraphBuilder.koinFindIdNavigation(
    navController: NavController
) {
    composable(
        route = FindIdNavType.Verification.route
    ) {
        FindIdVerification {
            navController.navigate("${FindIdNavType.Complete.route}/$it")
        }
    }

    composable(
        route = "${FindIdNavType.Complete.route}/{$LOGIN_ID}",
        arguments = listOf(
            navArgument(LOGIN_ID) { NavType.StringType }
        )
    ) {
        val loginId = it.arguments?.getString(LOGIN_ID) ?: ""

        FindIdComplete(loginId)
    }
}

const val LOGIN_ID = "loginId"
