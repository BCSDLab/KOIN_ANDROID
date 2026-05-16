package `in`.koreatech.koin.feature.user.ui.findpassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.user.ui.findpassword.changepassword.ChangePasswordScreen
import `in`.koreatech.koin.feature.user.ui.findpassword.complete.FindPasswordCompleteScreen
import `in`.koreatech.koin.feature.user.ui.findpassword.verification.FindPasswordVerification

fun NavGraphBuilder.koinFindPasswordGraph(
    navController: NavController
) {
    composable<FindPasswordNavType.Verification> {
        FindPasswordVerification(
            navigateToPasswordScreen = { loginId, verificationMethod ->
                navController.navigate(FindPasswordNavType.ChangePassword(loginId = loginId, verificationMethod = verificationMethod))
            }
        )
    }

    composable<FindPasswordNavType.ChangePassword> {
        ChangePasswordScreen {
            navController.navigate(FindPasswordNavType.Complete)
        }
    }

    composable<FindPasswordNavType.Complete> {
        FindPasswordCompleteScreen()
    }
}

const val VERIFICATION_METHOD = "verificationMethod"
const val LOGIN_ID = "loginId"
