package `in`.koreatech.koin.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.feature.signup.ui.term.SignUpTermScreen
import `in`.koreatech.koin.feature.signup.ui.userinfo.general.SignUpGeneralUserInfo
import `in`.koreatech.koin.feature.signup.ui.userinfo.student.SignUpStudentUserInfo
import `in`.koreatech.koin.feature.signup.ui.usertype.SignUpUserType
import `in`.koreatech.koin.feature.signup.ui.verification.SignUpVerification

fun NavGraphBuilder.koinSignUpGraph(
    navController: NavController
) {
    composable(
        route = SignUpNavType.Term.route
    ) {

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

