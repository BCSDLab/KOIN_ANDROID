package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.TimetableInputField
import `in`.koreatech.koin.feature.timetable.component.TimetableTimeContentRow
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun BottomSheetCustomContent(
    customExtraContentState: CustomExtraContentState,
    modifier: Modifier = Modifier,
    onPlaceNameChange: (id: Int, text: String) -> Unit = { _, _ -> },
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = {},
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimetableTimeContentRow(
            customContent = customExtraContentState,
            onDayOfWeekChange = onDayOfWeekChange,
            onClickStartTime = onClickStartTime,
            onClickEndTime = onClickEndTime
        )
        TimetableInputField(
            text = customExtraContentState.place,
            title = stringResource(id = R.string.timetable_input_field_title_place),
            onValueChange = {
                onPlaceNameChange(customExtraContentState.id, it)
            }
        )
    }
}

@Preview
@Composable
private fun BottomSheetCustomContentPreview() {
    BottomSheetCustomContent(
        customExtraContentState = CustomExtraContentState()
    )
}
