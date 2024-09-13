package `in`.koreatech.business.feature.findpassword.navigator


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.findpassword.changepassword.ChangePasswordScreenImpl
import `in`.koreatech.business.feature.findpassword.finishchangepassword.FinishChangePasswordScreen
import `in`.koreatech.business.feature.findpassword.passwordauthentication.PasswordAuthenticationScreenImpl


@Composable
fun ChangePassword(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ChangePasswordRoute.Authentication.name,
        modifier = modifier
    ) {
        composable(route = ChangePasswordRoute.Authentication.name) {
            PasswordAuthenticationScreenImpl(
                navigateToChangePassword = {
                    navigateToRandomScreen(navController, it)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
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
                navigateToFinish = {
                    navController.navigate(ChangePasswordRoute.Finish.name)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = ChangePasswordRoute.Finish.name) {
            FinishChangePasswordScreen()
        }
    }
}

private fun navigateToRandomScreen(
    navController: NavController,
    phoneNumber: String
) {
    navController.navigate("${ChangePasswordRoute.ChangePassword}/${phoneNumber}")
}