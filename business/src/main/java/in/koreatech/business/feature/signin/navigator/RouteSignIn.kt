package `in`.koreatech.business.feature.signin.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.findpassword.navigator.ChangePassword
import `in`.koreatech.business.feature.signin.SignInScreenImpl
import `in`.koreatech.business.feature.signup.navigator.SignupNavigator

@Composable
fun SignInNavigator(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SignInNavigator.SignIn.name,
        modifier = modifier
    ){
        composable(route = SignInNavigator.SignIn.name){
            SignInScreenImpl(
                navigateToMain = {},
                navigateToSignUp = {
                    navController.navigate(SignInNavigator.SignUp.name)
                },
                navigateToFindPassword = {
                    navController.navigate(SignInNavigator.FindPassword.name)
                }
            )
        }

        composable(route = SignInNavigator.SignUp.name){
            SignupNavigator(
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(route = SignInNavigator.FindPassword.name){
            ChangePassword()
        }
    }
}