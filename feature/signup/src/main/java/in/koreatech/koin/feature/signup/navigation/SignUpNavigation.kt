package `in`.koreatech.koin.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.signup.ui.term.SignUpTermScreen
import `in`.koreatech.koin.feature.signup.ui.verification.SignUpVerification

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
        SignUpVerification { name, phoneNumber, gender ->
            navController.navigate("${SignUpNavType.UserType.route}/$name/$phoneNumber/$gender")
        }
    }
    composable(
        route = "${SignUpNavType.UserType.route}/{$NAME}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(NAME) { type = NavType.StringType },
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
    }
    composable(
        route = "${SignUpNavType.GeneralUserInfo.route}/{$NAME}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(NAME) { type = NavType.StringType },
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
    }

    composable(
        route = "${SignUpNavType.StudentUserInfo.route}/{$NAME}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(NAME) { type = NavType.StringType },
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
    }

    composable(
        route = SignUpNavType.SignUpComplete.route
    ) {
    }
}

const val PHONE_NUMBER = "phoneNumber"
const val NAME = "name"
const val GENDER = "gender"
