package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.BottomNavItem

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = RebrandKoinTheme.colors.neutral0,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .clickable {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = iconNameToDrawableRes(item.iconName)),
                        contentDescription = item.label,
                        modifier = Modifier.padding(top = 9.dp),
                        tint = if (currentDestination?.hierarchy?.any { it.route == item.route } == true) {
                            RebrandKoinTheme.colors.primary500
                        } else {
                            RebrandKoinTheme.colors.neutral300
                        }
                    )
                    Text(
                        item.label,
                        style = RebrandKoinTheme.typography.bold12,
                        fontSize = 12.sp,
                        color = if (currentDestination?.hierarchy?.any { it.route == item.route } == true) {
                            RebrandKoinTheme.colors.neutral800
                        } else {
                            KoinTheme.colors.neutral300
                        }
                    )
                }

                if (index < items.lastIndex) {
                    Spacer(modifier = Modifier.width(84.dp))
                }
            }
        }
    }
}
