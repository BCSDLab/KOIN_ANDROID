package `in`.koreatech.koin.feature.findpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.findpassword.ui.sms.FindPasswordBySms

fun NavGraphBuilder.koinFindPasswordGraph(
    navController: NavController
) {
    composable(
        route = FindPasswordNavType.SmsVerification.route
    ) {
        FindPasswordBySms()
    }

    composable(
        route = FindPasswordNavType.EmailVerification.route
    ) {

    }

    composable(
        route = FindPasswordNavType.ChangePassword.route
    ) {

    }

    composable(
        route = FindPasswordNavType.Complete.route
    ) {

    }
}