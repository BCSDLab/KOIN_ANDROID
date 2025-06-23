package `in`.koreatech.koin.ui.store.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.koin.domain.model.store.BottomNavItem
import `in`.koreatech.koin.ui.store.components.BottomNavigationBar
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel

@Composable
fun StoreMainScreen(
    categoryId: Int
) {
    val navController = rememberNavController()
    val storeViewModel: StoreViewModel = hiltViewModel()
    val context = LocalContext.current

    val items = listOf(
        BottomNavItem("홈", "home", "home"),
        BottomNavItem("주변 상점", "nearby", "nearby"),
        BottomNavItem("주문 내역", "orderHistory", "orderHistory")
    )

    BackHandler {
        val popped = navController.popBackStack()
        if (!popped) {
            (context as? ComponentActivity)?.finish()
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = items,
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen(
                        categoryId = categoryId,
                        viewModel = storeViewModel,
                        onNavigationClick = {
                            val popped = navController.popBackStack()
                            if (!popped) {
                                (context as? ComponentActivity)?.finish()
                            }
                        },
                        onCartClick = { }
                    )
                }
                composable("nearby") { NearbyStoreScreen() }
//                composable("orders") { OrdersScreen() }
                composable("orderHistory") { MenuOptionScreen() }
            }
        }
    )
}
