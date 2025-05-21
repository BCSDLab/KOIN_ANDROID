package `in`.koreatech.koin.feature.club.ui.detail.component.tabrow

import androidx.compose.foundation.layout.height
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow

@Composable
fun DetailTabRow (
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit,
    titles: List<String>
){
    KoinTabRow(
        selectedTabIndex = selectedTabIndex,
        onTabSelected = onTabSelected,
        titles = titles,
        indicatorColor = Color(0xFFB611F5), // Non theme data
        selectedTextColor = Color(0xFFB611F5), // Non theme data
        indicator = @Composable { tabPositions ->
            if(selectedTabIndex < tabPositions.size)
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(1.dp),
                    color = Color(0xFFB611F5)
                )
        },
        divider = {}
    )
}