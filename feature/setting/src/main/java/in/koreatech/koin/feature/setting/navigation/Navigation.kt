package `in`.koreatech.koin.feature.setting.navigation

import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import `in`.koreatech.koin.feature.setting.ui.SettingScreen

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
            }
        )
    }
}
