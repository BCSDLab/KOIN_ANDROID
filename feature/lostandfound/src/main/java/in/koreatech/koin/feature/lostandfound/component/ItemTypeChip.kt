package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ItemTypeChip(
    chipItemList: ImmutableList<LostItemCategory>,
    modifier: Modifier = Modifier,
    selectedChipIndexes: Int? = null,
    onChipSelected: (index: Int) -> Unit = {}
) {
    LostAndFoundTextChipFlowGroup(
        titles = chipItemList.map { stringResource(it.stringRes) },
        selectedChipIndexes = if (selectedChipIndexes != null) listOf(selectedChipIndexes) else listOf(),
        onChipSelected = { onChipSelected(it) },
        showClickRipple = false,
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        chipColors = keywordChipColors(),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    )
}
