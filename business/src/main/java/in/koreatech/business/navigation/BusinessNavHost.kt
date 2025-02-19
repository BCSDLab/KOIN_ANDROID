package `in`.koreatech.business.navigation

import android.content.Context
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import `in`.koreatech.business.feature.event.writeevent.writeevent.registerEventScreen
import `in`.koreatech.business.feature.findpassword.navigator.ChangePasswordRoute
import `in`.koreatech.business.feature.findpassword.navigator.changePasswordScreen
import `in`.koreatech.business.feature.insertstore.navigator.registerStoreScreen
import `in`.koreatech.business.feature.signin.navigator.signInScreen
import `in`.koreatech.business.feature.signup.navigator.signUpScreen
import `in`.koreatech.business.feature.store.navigator.myStoreScreen
import `in`.koreatech.business.feature.storemenu.modifymenu.navigator.modifyMenuScreen
import `in`.koreatech.business.feature.storemenu.registermenu.navigator.registerMenuScreen
import `in`.koreatech.business.feature.storemenu.managemenu.navigator.manageMenuScreen

const val SIGNINSCREEN = "sign_in_screen"
const val SIGNUPSCREEN = "sign_up_screen"
const val MYSTORESCREEN = "my_store_screen"
const val REGISTERSTORESCREEN = "register_store_screen"
const val ADDEVENT = "register_event_screen"
const val MANAGEMENUSCREEN = "manage_menu_screen"
const val MODIFYMENUSCREEN = "modify_menu_screen"
const val REGISTERMENUSCREEN = "register_menu_screen"
const val CHANGEPASSWORDSCREEN = "change_password_screen"
const val EMPTYTOKEN = "empty_token"
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KoinBusinessNavHost(
    startDestination: String = "",
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        signInScreen(
            navigateToMain = { isFirst ->
                if(isFirst) navController.navigate(REGISTERSTORESCREEN)
                else navController.navigate(MYSTORESCREEN)
            },
            navigateToSignUp= { navController.navigate(SIGNUPSCREEN) },
            navigateToFindPassword = {navController.navigate(CHANGEPASSWORDSCREEN) }
        )

        signUpScreen(
            navController = navController
        )

        registerStoreScreen(
            navController = navController
        )

        myStoreScreen(
            navController = navController
        )

        registerEventScreen(
            navController = navController
        )

        manageMenuScreen(
            navController = navController
        )

        modifyMenuScreen(
            navController = navController
        )

        registerMenuScreen(
            navController = navController
        )

        changePasswordScreen(
            navigateToFinish = { navController.navigate(ChangePasswordRoute.Finish.name) },
            navigateToSignInScreen = { navController.navigate(SIGNINSCREEN){
                popUpTo(CHANGEPASSWORDSCREEN){
                    inclusive = true
                }
            } },
            onBackPressed = { navController.navigateUp() },
            navigateToChangeScreen = {
                navigateToScreenWithStringArgument(
                    navController = navController,
                    route = ChangePasswordRoute.ChangePassword.name,
                    argument = it
                )
            }
        )
    }
}


fun NavController.toNavigateScreenWithStoreId(screenRoute: String, storeId: Int) {
    val bundle = Bundle().apply {
        putInt("menuId", storeId)
    }

    navigate("${screenRoute}/$storeId", bundle)
}

fun NavController.toNavigateScreenWithMenuId(screenRoute: String, menuId: Int) {
    val bundle = Bundle().apply {
        putInt("menuId", menuId)
    }

    navigate("${screenRoute}/$menuId", bundle)
}

fun NavController.toNavigateRegisterMenuScreen(storeId: Int) {
    val bundle = Bundle().apply {
        putInt("storeId", storeId)
    }
    navigate(REGISTERMENUSCREEN, bundle)
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

private fun navigateToScreenWithStringArgument(
    navController: NavController,
    route: String,
    argument: String,
) {
    navController.navigate("${route}/${argument}")
}

private fun getToken(
    context: Context
):String{
    var masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    var sharedOwnerPreferences = EncryptedSharedPreferences.create(
        context,
        "token",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return sharedOwnerPreferences.getString("accessToken", null) ?: EMPTYTOKEN
}


