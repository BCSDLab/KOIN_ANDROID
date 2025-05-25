package `in`.koreatech.koin.feature.club.ui.detail.component.tabrow

import androidx.compose.foundation.layout.height
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DetailTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
    titles: List<String>
) {
    KoinTabRow(
        selectedTabIndex = selectedTabIndex,
        onTabSelected = onTabSelected,
        titles = titles,
        indicatorColor = KoinTheme.colors.primary500,
        selectedTextColor = KoinTheme.colors.primary500,
        indicator = @Composable { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(1.dp),
                    color = KoinTheme.colors.primary500
                )
            }
        },
        divider = {}
    )
}
