package `in`.koreatech.business.feature.signup.navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupScreen
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthScreen
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthViewModel
import `in`.koreatech.business.feature.signup.businessauth.SearchStoreScreen
import `in`.koreatech.business.feature.signup.checkterm.CheckTermScreen
import `in`.koreatech.business.feature.signup.completesignup.CompleteSignupScreen


@Composable
fun SignupNavigator(
    modifier: Modifier,
    accountSetupViewModel: AccountSetupViewModel = hiltViewModel(),
    businessAuthViewModel: BusinessAuthViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SignupRoute.TERMS_OF_SERVICE.name,
        modifier = modifier.padding(16.dp)
    ) {

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
                onBackClicked = { navController.navigate(SignupRoute.LOGIN.name) }
            )
        }
    }
}