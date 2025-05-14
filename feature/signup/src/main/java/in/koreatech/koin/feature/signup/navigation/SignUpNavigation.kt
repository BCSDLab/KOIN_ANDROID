package `in`.koreatech.koin.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.signup.ui.complete.SignUpCompleteScreen
import `in`.koreatech.koin.feature.signup.ui.term.SignUpTermScreen
import `in`.koreatech.koin.feature.signup.ui.userinfo.general.SignUpGeneralUserInfo
import `in`.koreatech.koin.feature.signup.ui.userinfo.student.SignUpStudentUserInfo
import `in`.koreatech.koin.feature.signup.ui.usertype.SignUpUserType
import `in`.koreatech.koin.feature.signup.ui.verification.SignUpVerification

fun NavGraphBuilder.koinSignUpGraph(
    navController: NavController,
    finish: () -> Unit = {}
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
        SignUpVerification { phoneNumber, gender ->
            navController.navigate("${SignUpNavType.UserType.route}/$phoneNumber/$gender")
        }
    }
    composable(
        route = "${SignUpNavType.UserType.route}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
        val phoneNumber = it.arguments?.getString(PHONE_NUMBER) ?: ""
        val gender = it.arguments?.getString(GENDER) ?: ""
        SignUpUserType(
            navigateToStudentScreen = {
                navController.navigate("${SignUpNavType.StudentUserInfo.route}/$phoneNumber/$gender")
            },
            navigateToGeneralScreen = {
                navController.navigate("${SignUpNavType.GeneralUserInfo.route}/$phoneNumber/$gender")
            }
        )
    }
    composable(
        route = "${SignUpNavType.GeneralUserInfo.route}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
        SignUpGeneralUserInfo {
            navController.navigate(SignUpNavType.SignUpComplete.route)
        }
    }

    composable(
        route = "${SignUpNavType.StudentUserInfo.route}/{$PHONE_NUMBER}/{$GENDER}",
        arguments = listOf(
            navArgument(PHONE_NUMBER) { type = NavType.StringType },
            navArgument(GENDER) { type = NavType.StringType }
        )
    ) {
        SignUpStudentUserInfo {
            navController.navigate(SignUpNavType.SignUpComplete.route)
        }
    }

    composable(
        route = SignUpNavType.SignUpComplete.route
    ) {
        SignUpCompleteScreen {
            finish()
        }
    }
}

const val PHONE_NUMBER = "phoneNumber"
const val GENDER = "gender"
