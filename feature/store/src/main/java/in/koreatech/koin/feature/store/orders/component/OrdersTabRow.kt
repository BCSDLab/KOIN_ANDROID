package `in`.koreatech.koin.feature.store.orders.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun OrdersTabRow(
    title: List<String>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit = { },
    containerColor: Color = Color.White,
    indicatorColor: Color = RebrandKoinTheme.colors.primary500,
    selectedTextColor: Color = RebrandKoinTheme.colors.primary500,
    unselectedTextColor: Color = RebrandKoinTheme.colors.neutral500,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        if (selectedTabIndex < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .padding(horizontal = 8.dp),
                color = indicatorColor
            )
        }
    },
    divider: @Composable () -> Unit = @Composable {
        HorizontalDivider(
            color = KoinTheme.colors.neutral400
        )
    }
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        indicator = indicator,
        divider = divider,
        tabs = {
            title.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    selectedContentColor = selectedTextColor,
                    unselectedContentColor = unselectedTextColor,
                    onClick = { onSelected(index) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        style = RebrandKoinTheme.typography.bold16,
                        text = title
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun OrdersTabRowPreview() {
    OrdersTabRow(
        title = listOf(
            "test",
            "test"
        ),
        selectedTabIndex = 0
    )
}
