package `in`.koreatech.koin.feature.store.orders.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults

@Composable
fun FilterTextChipSelect(
    title: String,
    selected: Int,
    defaultIdx: Int,
    chipItem: List<Int>,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = RebrandKoinTheme.typography.bold14,
            color = RebrandKoinTheme.colors.neutral600
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            chipItem.forEachIndexed { idx, title ->
                val isSelected = idx == selected
                FilterTextChip(
                    title = stringResource(id = title),
                    isSelected = isSelected,
                    onClick = { onValueChange(idx) },
                    onCancel = { onValueChange(defaultIdx) }
                )
            }
        }
    }
}

@Composable
fun FilterTextChip(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    KoinStoreChip(
        modifier = modifier,
        text = title,
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            borderWidth = if (isSelected) 1.dp else 0.dp,
            borderColor = RebrandKoinTheme.colors.neutral300,
            containerColor = if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0,
            textColor = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500,
            paddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
        ),
        onClick = if (isSelected) onCancel else onClick
    )
}
