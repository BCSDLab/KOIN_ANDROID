package `in`.koreatech.business.feature.signup.navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature.signup.accountauth.EmailAuthScreen
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupScreen
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthScreen
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthViewModel
import `in`.koreatech.business.feature.signup.businessauth.SearchStoreScreen
import `in`.koreatech.business.feature.signup.completesignup.CompleteSignupScreen


@Composable
fun SignupNavigator(
    modifier: Modifier,
    accountSetupViewModel: AccountSetupViewModel= hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel(),) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SignupRoute.BASIC_INFO_INPUT.name,
        modifier = Modifier.padding(16.dp)
    ) {

        composable(
            route = SignupRoute.BASIC_INFO_INPUT.name,
        ) {
            AccountSetupScreen(
                onBackClicked = { navController.popBackStack() },
                onNextClicked = { email->
                    navController.navigate("${SignupRoute.EMAIL_AUTH.name}/$email")

                },
                viewModel = accountSetupViewModel
            )
        }

        composable(
            route = "${SignupRoute.EMAIL_AUTH.name}/{email}",
        ) {
            val email = it.arguments?.getString("email") ?: ""
            EmailAuthScreen(
                email = email,
                onBackClicked = { navController.popBackStack() },
                onNextClicked = {
                    navController.navigate(SignupRoute.BUSINESS_AUTH.name)
                },
            )
        }

        composable(
            route = SignupRoute.BUSINESS_AUTH.name,
        ) {
            BusinessAuthScreen(
                accountSetupViewModel= accountSetupViewModel,
                businessAuthViewModel = businessAuthViewModel,
                onBackClicked = { navController.popBackStack() },
                onSearchClicked ={
                    navController.navigate(SignupRoute.STORE_SETUP.name)
                },
            ) {
                navController.navigate(SignupRoute.SIGNUP_COMPLETED.name)
            }
        }

        composable(
            route = SignupRoute.STORE_SETUP.name,
        ) {
            SearchStoreScreen(
                businessAuthViewModel = businessAuthViewModel,
                onBackClicked = {
                    navController.popBackStack() },)
        }


        composable(
            route = SignupRoute.SIGNUP_COMPLETED.name,
        ) {
            CompleteSignupScreen(
                onBackClicked = { navController.navigate(SignupRoute.LOGIN.name) }
            )
        }


    }
}