package `in`.koreatech.koin.feature.findpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.findpassword.ui.changepassword.ChangePasswordScreen
import `in`.koreatech.koin.feature.findpassword.ui.complete.FindPasswordCompleteScreen
import `in`.koreatech.koin.feature.findpassword.ui.verification.FindPasswordVerification

fun NavGraphBuilder.koinFindPasswordGraph(
    navController: NavController
) {
    composable(
        route = FindPasswordNavType.Verification.route
    ) {
        FindPasswordVerification(
            navigateToPasswordScreen = { loginId, verificationMethod ->
                navController.navigate("${FindPasswordNavType.ChangePassword.route}/$loginId/$verificationMethod")
            },
        )
    }

    composable(
        route = "${FindPasswordNavType.ChangePassword.route}/{$LOGIN_ID}/{$VERIFICATION_METHOD}",
        arguments = listOf(
            navArgument(LOGIN_ID) { type = NavType.StringType },
            navArgument(VERIFICATION_METHOD) { type = NavType.StringType }
        )
    ) {
        ChangePasswordScreen {
            navController.navigate(FindPasswordNavType.Complete.route)
        }
    }

    composable(
        route = FindPasswordNavType.Complete.route
    ) {
        FindPasswordCompleteScreen()
    }
}

const val VERIFICATION_METHOD = "verificationMethod"
const val LOGIN_ID = "loginId"
