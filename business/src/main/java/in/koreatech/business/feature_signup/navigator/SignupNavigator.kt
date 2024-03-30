package `in`.koreatech.business.feature_signup.navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import `in`.koreatech.business.feature_signup.accountauth.AccountAuthScreen
import `in`.koreatech.business.feature_signup.accountsetup.AccountSetupScreen
import `in`.koreatech.business.feature_signup.businessauth.BusinessAuthScreen
import `in`.koreatech.business.feature_signup.businessauth.SearchStoreScreen
import `in`.koreatech.business.feature_signup.completesignup.CompleteSignupScreen


@Composable
fun SignupNavigator(modifier: Modifier) {
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
                onNextClicked = {
                    navController.navigate("${SignupRoute.ACCOUNT_AUTH.name}/$it")
                },
            )
        }

        composable(
            route = "${SignupRoute.ACCOUNT_AUTH.name}/{email}",

            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val email = it.arguments?.getString("email") ?: ""
            AccountAuthScreen(
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
                onBackClicked = { navController.popBackStack() },
                onSearchClicked = {
                    navController.navigate(SignupRoute.STORE_SETUP.name)
                },
                onNextClicked = {
                    navController.navigate(SignupRoute.SIGNUP_COMPLETED.name)
                },
            )
        }

        composable(
            route = SignupRoute.STORE_SETUP.name,
        ) {
            SearchStoreScreen(
                onBackClicked = { navController.popBackStack() })
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