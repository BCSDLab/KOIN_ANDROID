package `in`.koreatech.bus.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun ShuttleBusOperationChip(
    operationType: ShuttleBusOperationType,
    modifier: Modifier = Modifier
) {
    ReadOnlyTextChip(
        modifier = modifier,
        title = stringResource(operationType.simpleTitleRes),
        containerColor = when (operationType) {
            ShuttleBusOperationType.WEEKDAY -> Color(0xFFFFF9EE)
            ShuttleBusOperationType.WEEKEND -> Color(0xFFE4F2FF)
            ShuttleBusOperationType.CIRCULATION -> Color(0xFFE5F5EC)
            else -> Color.Transparent
        },
        textStyle = RebrandKoinTheme.typography.medium12.copy(
            color = when (operationType) {
                ShuttleBusOperationType.WEEKDAY -> Color(0xFFFFAD0D)
                ShuttleBusOperationType.WEEKEND -> Color(0xFF3A70E2)
                ShuttleBusOperationType.CIRCULATION -> Color(0xFF0C9D61)
                else -> Color.Transparent
            },
            fontSize = 10.sp
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
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
