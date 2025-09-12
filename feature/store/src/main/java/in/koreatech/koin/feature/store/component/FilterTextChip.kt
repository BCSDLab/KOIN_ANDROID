package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun FilterTextChipSelect(
    title: String,
    selected: Int,
    defaultIdx: Int,
    chipTitle: List<Pair<Int, Boolean>>,
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
            chipTitle.forEachIndexed { idx, (title, default) ->
                if (!default) {
                    val isSelected = idx == selected
                    FilterTextChip(
                        title = stringResource(id = title),
                        isSelected = isSelected,
                        onClick = { onValueChange(idx) },
                        onCancle = { onValueChange(defaultIdx) }
                    )
                }
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
    onCancle: () -> Unit = {}
) {
    TextChip(
        title = title,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .then(
                if (isSelected) {
                    Modifier
                } else {
                    Modifier
                        .border(
                            width = 1.dp,
                            color = RebrandKoinTheme.colors.neutral300,
                            shape = CircleShape
                        )
                }
            ),
        isSelected = isSelected,
        onSelect = { if (isSelected) onCancle() else onClick() },
        chipColors = TextChipDefaults.chipColors(
            RebrandKoinTheme.colors.primary500,
            RebrandKoinTheme.colors.neutral0,
            RebrandKoinTheme.colors.neutral0,
            RebrandKoinTheme.colors.neutral500
        )
    )
}
