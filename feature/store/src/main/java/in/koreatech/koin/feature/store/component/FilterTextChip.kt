package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.OrderFilterEnum

@Composable
fun <T> FilterTextChipSelect(
    title: String,
    value: T,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : OrderFilterEnum {
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
            value::class.java.enumConstants
                .forEach { item ->
                    if (!item.isDefault) {
                        val isSelected = item == value
                        FilterTextChip(
                            title = stringResource(id = item.stringRes),
                            isSelected = isSelected,
                            onClick = { }
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
    onClick: () -> Unit = {}
) {
    TextChip(
        title = title,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RebrandKoinTheme.shapes.medium,
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .clickable { onClick() }
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
        onSelect = {},
        chipColors = TextChipDefaults.chipColors(
            RebrandKoinTheme.colors.primary500,
            RebrandKoinTheme.colors.neutral0,
            RebrandKoinTheme.colors.neutral100,
            RebrandKoinTheme.colors.neutral500
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterTextChipSelectPreview() {
    FilterTextChipSelect(
        title = "주소",
        value = LocationOption.CAMPUS
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterTextChipSelectPreview2() {
    FilterTextChipSelect(
        title = "주소",
        value = LocationOption.OUTSIDE
    )
}
