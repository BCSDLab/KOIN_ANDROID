package `in`.koreatech.koin.feature.department.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.screen.detail.DepartmentDetailScreen
import `in`.koreatech.koin.feature.department.screen.list.DepartmentListScreen
import `in`.koreatech.koin.feature.department.util.findActivity

@Composable
fun DepartmentNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    NavHost(
        modifier = modifier.background(RebrandKoinTheme.colors.neutral0),
        navController = navController,
        startDestination = Routes.DepartmentList
    ) {
        composable<Routes.DepartmentList> {
            DepartmentListScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = { context.findActivity()?.finish() },
                navigateToDetail = { category ->
                    navController.navigate(Routes.DepartmentDetail(category))
                }
            )
        }

        composable<Routes.DepartmentDetail> {
            DepartmentDetailScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigationIconClick = { navController.popBackStack() }
            )
        }
    }
}
