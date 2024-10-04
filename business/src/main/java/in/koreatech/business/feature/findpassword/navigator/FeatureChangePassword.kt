package `in`.koreatech.business.feature.findpassword.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import `in`.koreatech.business.feature.findpassword.changepassword.ChangePasswordScreenImpl
import `in`.koreatech.business.feature.findpassword.finishchangepassword.FinishChangePasswordScreen
import `in`.koreatech.business.feature.findpassword.passwordauthentication.PasswordAuthenticationScreenImpl
import `in`.koreatech.business.navigation.CHANGEPASSWORDSCREEN
import `in`.koreatech.business.navigation.SIGNINSCREEN


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.changePasswordScreen(
    navigateToFinish: () -> Unit = {},
    navigateToSignInScreen: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    navigateToChangeScreen: (String) -> Unit = {}
){
    navigation(
        route = CHANGEPASSWORDSCREEN,
        startDestination = ChangePasswordRoute.Authentication.name
    ){
        composable(route = ChangePasswordRoute.Authentication.name) {
            PasswordAuthenticationScreenImpl(
                navigateToChangePassword = navigateToChangeScreen,
                onBackPressed = onBackPressed
            )
        }

        composable(
            route = "${ChangePasswordRoute.ChangePassword.name}/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ChangePasswordScreenImpl(
                navigateToFinish = navigateToFinish,
                onBackPressed = onBackPressed
            )
        }

        composable(route = ChangePasswordRoute.Finish.name) {
            FinishChangePasswordScreen(
                navigateToSignInScreen = navigateToSignInScreen
            )
        }
    }
}