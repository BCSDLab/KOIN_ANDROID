package `in`.koreatech.business.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.business.feature.storemenu.modifymenu.navigator.modifyMenuScreen
import `in`.koreatech.business.feature.storemenu.registermenu.navigator.registerMenu


const val SIGNINSCREEN = "sign_in_screen"
const val SIGNUPSCREEN = "sign_up_screen"
const val MODIFYMENUSCREEN = "modify_menu_screen"
const val REGISTERMENUSCREEN = "register_menu_screen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KoinBusinessNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SIGNINSCREEN,
    ) {
        composable(
            route = SIGNINSCREEN,
        ) {
                /*TestScreen(
                    onSignUpComplete = {
                        navController.navigate(REGISTERMENUSCREEN)
                    }
                )*/
        } //Todo 네비게이션 통합할때 삭제하기

        modifyMenuScreen(
            navController = navController
        )

        registerMenu(
            navController = navController
        )
    }
}

fun NavController.toNavigateModifyMenuScreen(menuId: Int) {
    val bundle = Bundle().apply {
        putInt("menuId", menuId)
    }
    navigate("${MODIFYMENUSCREEN}/$menuId", bundle)
}

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route = route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}


