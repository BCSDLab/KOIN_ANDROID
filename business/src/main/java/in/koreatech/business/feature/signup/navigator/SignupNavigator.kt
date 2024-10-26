package `in`.koreatech.business.feature.signup.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupScreen
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthScreen
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthViewModel
import `in`.koreatech.business.feature.signup.businessauth.SearchStoreScreen
import `in`.koreatech.business.feature.signup.checkterm.CheckTermScreen
import `in`.koreatech.business.feature.signup.completesignup.CompleteSignupScreen
import `in`.koreatech.business.navigation.SIGNINSCREEN
import `in`.koreatech.business.navigation.SIGNUPSCREEN
import `in`.koreatech.business.navigation.sharedHiltViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.signUpScreen(
    navController: NavController
){
    navigation(
        route = SIGNUPSCREEN,
        startDestination = SignupRoute.TERMS_OF_SERVICE.name
    ){
        composable(
            route = SignupRoute.TERMS_OF_SERVICE.name,
        ) {
            CheckTermScreen(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = { navController.navigate(SignupRoute.BASIC_INFO_INPUT.name) }
            )
        }

        composable(
            route = SignupRoute.BASIC_INFO_INPUT.name,
        ) {
            val accountSetupViewModel: AccountSetupViewModel = it.sharedHiltViewModel(navController = navController)
            AccountSetupScreen(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = {
                    navController.navigate(SignupRoute.BUSINESS_AUTH.name)
                },
                viewModel = accountSetupViewModel
            )
        }

        composable(
            route = SignupRoute.BUSINESS_AUTH.name,
        ) {
            val accountSetupViewModel: AccountSetupViewModel = it.sharedHiltViewModel(navController = navController)
            val businessAuthViewModel: BusinessAuthViewModel = it.sharedHiltViewModel(navController = navController)
            BusinessAuthScreen(
                accountSetupViewModel = accountSetupViewModel,
                businessAuthViewModel = businessAuthViewModel,
                onBackClicked = { navController.popBackStack() },
                onSearchClicked = {
                    navController.navigate(SignupRoute.STORE_SETUP.name)
                },
            ) {
                navController.navigate(SignupRoute.SIGNUP_COMPLETED.name)
            }
        }

        composable(
            route = SignupRoute.STORE_SETUP.name,
        ) {
            val businessAuthViewModel: BusinessAuthViewModel = it.sharedHiltViewModel(navController = navController)
            SearchStoreScreen(
                businessAuthViewModel = businessAuthViewModel,
                onBackButtonClicked = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = SignupRoute.SIGNUP_COMPLETED.name,
        ) {
            CompleteSignupScreen(
                onBackClicked = {
                    navController.navigate(SIGNINSCREEN){
                    popUpTo(
                        SIGNUPSCREEN
                    ){
                        inclusive = true
                    }
                } },
                onNavigateToLoginScreen = {
                    navController.navigate(SIGNINSCREEN){
                        popUpTo(
                            SIGNUPSCREEN
                        ){
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}