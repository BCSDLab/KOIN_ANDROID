package `in`.koreatech.koin.feature.setting.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.koreatech.koin.feature.setting.constant.TermConstant
import `in`.koreatech.koin.feature.setting.ui.SettingScreen
import `in`.koreatech.koin.feature.setting.ui.term.TermScreen

fun NavGraphBuilder.koinSettingGraph(
    navController: NavController
) {
    composable(
        route = SettingNavType.Setting.route
    ) {
        val context = LocalContext.current

        SettingScreen(
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            },
            onPrivacyPolicyClick = {
                navController.navigate("${SettingNavType.Term.route}/${TermConstant.TERM_PRIVACY_POLICY.type}")
            },
            onKoinTermsClick = {
                navController.navigate("${SettingNavType.Term.route}/${TermConstant.TERM_KOIN.type}")
            },
            onMarketingTermsClick = {
                navController.navigate("${SettingNavType.Term.route}/${TermConstant.TERM_MARKETING.type}")
            }
        )
    }

    composable(
        route = "${SettingNavType.Term.route}/{${TERM_TYPE}}",
        arguments = listOf(
            navArgument(TERM_TYPE) { type = NavType.StringType },
        )
    ) {
        val context = LocalContext.current
        val termType = when (it.arguments?.getString(TERM_TYPE)) {
            TermConstant.TERM_KOIN.type -> TermConstant.TERM_KOIN
            TermConstant.TERM_PRIVACY_POLICY.type -> TermConstant.TERM_PRIVACY_POLICY
            TermConstant.TERM_MARKETING.type -> TermConstant.TERM_MARKETING
            else -> TermConstant.TERM_UNKNOWN
        }

        TermScreen(
            termType = termType,
            onTopbarBackClick = {
                if (!navController.popBackStack()) {
                    (context as? Activity)?.finish()
                }
            }
        )
    }
}

const val TERM_TYPE = "termType"
