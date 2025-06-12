package `in`.koreatech.koin.feature.findpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.findpassword.ui.changepassword.ChangePasswordScreen
import `in`.koreatech.koin.feature.findpassword.ui.email.FindPasswordByEmail
import `in`.koreatech.koin.feature.findpassword.ui.sms.FindPasswordBySms

fun NavGraphBuilder.koinFindPasswordGraph(
    navController: NavController
) {
    composable(
        route = FindPasswordNavType.SmsVerification.route
    ) {
        FindPasswordBySms(
            navigateToEmailScreen = {
                navController.navigate(FindPasswordNavType.EmailVerification.route)
            },
            navigateToPasswordScreen = {
                navController.navigate(FindPasswordNavType.ChangePassword.route)
            },
        )
    }

    composable(
        route = FindPasswordNavType.EmailVerification.route
    ) {
        FindPasswordByEmail()
    }

    composable(
        route = FindPasswordNavType.ChangePassword.route
    ) {
        ChangePasswordScreen()
    }

    composable(
        route = FindPasswordNavType.Complete.route
    ) {
    }
}
