package `in`.koreatech.bus.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun BusTypeChip(
    busType: BusType,
    modifier: Modifier = Modifier
) {
    ReadOnlyTextChip(
        modifier = modifier,
        title = stringResource(busType.titleRes),
        shape = RoundedCornerShape(100.dp),
        containerColor = when (busType) {
            BusType.SHUTTLE -> Color(0xFFFFF9EE)
            BusType.EXPRESS -> Color(0xFFE4F2FF)
            BusType.CITY -> Color(0xFFE5F5EC)
            else -> Color.Transparent
        },
        textStyle = RebrandKoinTheme.typography.medium12.copy(
            color = when (busType) {
                BusType.SHUTTLE -> Color(0xFFFFAD0D)
                BusType.EXPRESS -> Color(0xFF3A70E2)
                BusType.CITY -> Color(0xFF0C9D61)
                else -> Color.Transparent
            },
            fontSize = 10.sp
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    )
}

@Preview
@Composable
private fun BusTypeChipPreview() {
    BusTypeChip(busType = BusType.SHUTTLE)
}

@Preview
@Composable
private fun BusTypeChipPreview2() {
    BusTypeChip(busType = BusType.EXPRESS)
}

@Preview
@Composable
private fun BusTypeChipPreview3() {
    BusTypeChip(busType = BusType.CITY)
}
