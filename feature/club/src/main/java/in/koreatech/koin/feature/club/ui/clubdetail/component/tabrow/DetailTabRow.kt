package `in`.koreatech.koin.feature.club.ui.clubdetail.component.tabrow

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DetailTabRow(
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
    titles: List<String>,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    textStyle: TextStyle = KoinTheme.typography.medium16,
    indicatorColor: Color = KoinTheme.colors.primary500,
    selectedTextColor: Color = KoinTheme.colors.primary500,
    unselectedTextColor: Color = KoinTheme.colors.neutral500,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        if (selectedTabIndex < tabPositions.size) {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(1.dp),
                color = indicatorColor
            )
        }
    },
    divider: @Composable () -> Unit = @Composable { }
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
                    onClick = { onTabSelected(index) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = title,
                        style = textStyle
                    )
                }
            }
        }
    )
}
