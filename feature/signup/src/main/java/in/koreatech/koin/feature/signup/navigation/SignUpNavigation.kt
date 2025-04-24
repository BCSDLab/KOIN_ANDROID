package `in`.koreatech.koin.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.signup.ui.term.SignUpTermScreen

fun NavGraphBuilder.koinSignUpGraph(
    navController: NavController
) {
    composable(
        route = SignUpNavType.Term.route
    ) {
        SignUpTermScreen {
            navController.navigate(SignUpNavType.Verification.route)
        }
    }
    composable(
        route = SignUpNavType.Verification.route
    ) {
    }
    composable(
        route = SignUpNavType.UserType.route
    ) {
    }
    composable(
        route = SignUpNavType.GeneralUserInfo.route
    ) {
    }

    composable(
        route = SignUpNavType.StudentUserInfo.route
    ) {
    }

    composable(
        route = SignUpNavType.SignUpComplete.route
    ) {
    }
}
