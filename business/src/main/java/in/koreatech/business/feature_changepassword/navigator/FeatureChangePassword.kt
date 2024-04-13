package `in`.koreatech.business.feature_changepassword.navigator

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature_changepassword.changepassword.ChangePasswordScreenImpl
import `in`.koreatech.business.feature_changepassword.finishchangepassword.FinishChangePasswordScreen
import `in`.koreatech.business.feature_changepassword.passwordauthentication.PasswordAuthenticationScreen


@Composable
fun ChangePassword(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ChangePasswordRoute.Authentication.name,
        modifier = modifier
    ){
        composable(route = ChangePasswordRoute.Authentication.name){
            PasswordAuthenticationScreen(
                navigateToChangePassword = {
                    navigateToRandomScreen(navController, it)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = "${ChangePasswordRoute.ChangePassword.name}/{email}",
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){
            ChangePasswordScreenImpl(
                navigateToFinish = {
                    navController.navigate(ChangePasswordRoute.Finish.name)
                },
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = ChangePasswordRoute.Finish.name){
            FinishChangePasswordScreen()
        }
    }
}

private fun navigateToRandomScreen(
    navController: NavController,
    email: String
) {
    navController.navigate("${ChangePasswordRoute.ChangePassword}/${email}")
}