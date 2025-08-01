package `in`.koreatech.koin.feature.store

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.store.component.KoinStoreNavigationBar
import `in`.koreatech.koin.feature.store.component.KoinStoreNavigationBarItem
import `in`.koreatech.koin.feature.store.navigation.StoreNavType
import `in`.koreatech.koin.feature.store.navigation.koinStoreGraph
import `in`.koreatech.koin.feature.store.navigation.navigationBarItems
import `in`.koreatech.koin.feature.store.viewmodel.StoreViewModel

@AndroidEntryPoint
class StoreActivity : ComponentActivity() {
    private val viewModel by viewModels<StoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()

        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }

        val categoryId = intent.getIntExtra(STORE_CATEGORY, 1)

        setContent {
            val currentRoute by viewModel.currentRoute.collectAsState()

            RebrandKoinTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()

                LaunchedEffect(Unit) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        val route = backStackEntry.destination.route
                        if (route != null) {
                            viewModel.setCurrentRoute(
                                navigationBarItems.indexOfFirst { route.startsWith(it.type.route) }
                            )
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (currentBackStack?.destination?.route?.startsWith(StoreNavType.StoreMain.route) == true) {
                            KoinStoreNavigationBar {
                                navigationBarItems.forEachIndexed { index, item ->
                                    KoinStoreNavigationBarItem(
                                        selected = currentRoute == index,
                                        icon = painterResource(item.iconRes),
                                        label = stringResource(item.stringRes),
                                        onClick = {
                                            navController.navigate(item.type.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    containerColor = colorResource(id = R.color.store_detail_background),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = navController,
                        startDestination = StoreNavType.StoreMain.route
                    ) {
                        koinStoreGraph(
                            navController = navController,
                            categoryId = categoryId,
                            finish = { finish() }
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val STORE_CATEGORY = "STORE_CATEGORY"
    }
}
