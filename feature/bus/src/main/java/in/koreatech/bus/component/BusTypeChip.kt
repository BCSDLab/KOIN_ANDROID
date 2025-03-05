package `in`.koreatech.bus.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
internal fun BusTypeChip(
    busType: BusType,
    modifier: Modifier = Modifier,
) {
    ReadOnlyTextChip(
        modifier = modifier,
        title = stringResource(busType.titleRes),
        containerColor =
            when (busType) {
                BusType.SHUTTLE -> Color(0xFFFBEBD7)
                BusType.EXPRESS -> Color(0xFFD7E6FB)
                BusType.CITY -> Color(0xFFD7FBEB)
                else -> Color.Transparent
            },
        textStyle =
            KoinTheme.typography.regular12.copy(
                color = KoinTheme.colors.neutral600,
                fontSize = 11.sp,
            ),
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
