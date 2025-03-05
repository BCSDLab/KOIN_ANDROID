package `in`.koreatech.koin.core.designsystem.component.tab

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

/**
 * 텍스트 탭 레이아웃
 */
@Composable
fun KoinTabRow(
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
    titles: List<String>,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    indicatorColor: Color = KoinTheme.colors.primary600,
    selectedTextColor: Color = KoinTheme.colors.primary500,
    unselectedTextColor: Color = KoinTheme.colors.neutral500,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        if (selectedTabIndex < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = indicatorColor,
            )
        }
    },
    divider: @Composable () -> Unit = @Composable {
        HorizontalDivider(
            color = KoinTheme.colors.neutral400,
        )
    },
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        indicator = indicator,
        divider = divider,
        tabs = {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    selectedContentColor = selectedTextColor,
                    unselectedContentColor = unselectedTextColor,
                    onClick = { onTabSelected(index) },
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = title,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun KoinTabRowPreview() {
    KoinTabRow(
        selectedTabIndex = 0,
        titles = listOf("Tab1", "Tab2", "Tab3"),
        onTabSelected = {},
    )
}
