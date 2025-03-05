package `in`.koreatech.bus.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
internal fun ShuttleBusOperationChip(
    operationType: ShuttleBusOperationType,
    modifier: Modifier = Modifier,
) {
    ReadOnlyTextChip(
        modifier = modifier,
        title = stringResource(operationType.simpleTitleRes),
        containerColor =
            when (operationType) {
                ShuttleBusOperationType.WEEKEND -> Color(0xFF34ADFF)
                ShuttleBusOperationType.WEEKDAY -> Color(0xFFFFB443)
                ShuttleBusOperationType.CIRCULATION -> Color(0xFF4ED92C)
                else -> Color.Transparent
            },
        textStyle = KoinTheme.typography.regular12.copy(color = Color.White),
    )
}

@Preview
@Composable
private fun ShuttleBusOperationChipPreview() {
    ShuttleBusOperationChip(operationType = ShuttleBusOperationType.WEEKEND)
}

@Preview
@Composable
private fun ShuttleBusOperationChipPreview2() {
    ShuttleBusOperationChip(operationType = ShuttleBusOperationType.WEEKDAY)
}

@Preview
@Composable
private fun ShuttleBusOperationChipPreview3() {
    ShuttleBusOperationChip(operationType = ShuttleBusOperationType.CIRCULATION)
}
