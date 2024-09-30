package `in`.koreatech.business.feature.signin.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import `in`.koreatech.business.feature.signin.SignInScreen
import `in`.koreatech.business.navigation.SIGNINSCREEN

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.signInScreen(
    navigateToMain: (Boolean) -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    navigateToFindPassword: () -> Unit  = {}
){
    navigation(
        route = SIGNINSCREEN,
        startDestination =  SignInNavigator.SignIn.name
    ){
        composable(route = SignInNavigator.SignIn.name){
            SignInScreen(
                navigateToMain = navigateToMain,
                navigateToSignUp = navigateToSignUp,
                navigateToFindPassword = navigateToFindPassword
            )
        }
    }
}