package `in`.koreatech.koin.feature.findid.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.findid.ui.complete.FindIdComplete
import `in`.koreatech.koin.feature.findid.ui.verification.FindIdVerification

fun NavGraphBuilder.koinFindIdNavigation(
    navController: NavController
) {
    composable(
        route = FindIdNavType.Verification.route
    ) {
        FindIdVerification()
    }

    composable(
        route = "${FindIdNavType.Complete.route}/{$LOGIN_ID}",
        arguments = listOf(
            navArgument(LOGIN_ID) { NavType.StringType }
        )
    ) {
        val loginId = it.arguments?.getString(LOGIN_ID) ?: ""

        FindIdComplete(
            loginId
        )
    }
}

const val LOGIN_ID = "loginId"
