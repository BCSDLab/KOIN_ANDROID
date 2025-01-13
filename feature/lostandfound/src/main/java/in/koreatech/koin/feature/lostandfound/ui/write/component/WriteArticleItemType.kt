package `in`.koreatech.koin.feature.lostandfound.ui.write.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.ItemTypeChip
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory

@Composable
fun WriteArticleItemType(
    selectedChipIndex: Int,
    itemTypeRequired: Boolean = false,
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                style = KoinTheme.typography.medium14,
                text = stringResource(id = R.string.item_type),
            )

            if (itemTypeRequired) {
                LeadingIconText(
                    textStyle = KoinTheme.typography.medium12.copy(color = Color(0xFFF7941E)),
                    text = stringResource(id = R.string.item_type_required),
                    iconRes = R.drawable.ic_required,
                    iconSize = 16.dp
                )
            }
        }
        Text(
            style = KoinTheme.typography.regular12,
            text = stringResource(id = R.string.item_type_description),
            color = KoinTheme.colors.neutral500
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            ItemTypeChip(
                chipItemList = LostItemCategory.entries.filter { it != LostItemCategory.NONE },
                selectedChipIndexes = if (selectedChipIndex != -1) selectedChipIndex else null,
                onChipSelected = {
                    onItemSelected(it)
                }
            )
        }
    }
}
