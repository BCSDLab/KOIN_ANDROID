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
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun BottomSheetCustomContent(
    customContents: CustomContentState,
    modifier: Modifier = Modifier,
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onPlaceNameChange: (text: String) -> Unit = {},
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = {},
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimetableInputField(
            text = customContents.schedule,
            title = stringResource(id = R.string.timetable_input_field_title_schedule),
            optional = false,
            isError = customContents.isScheduleError,
            onValueChange = onScheduleNameChange
        )
        TimetableInputField(
            text = customContents.professor,
            title = stringResource(id = R.string.timetable_input_field_title_professor),
            onValueChange = onProfessorNameChange
        )
        TimetableTimeContentRow(
            customContent = customContents.time,
            onDayOfWeekChange = onDayOfWeekChange,
            onClickStartTime = onClickStartTime,
            onClickEndTime = onClickEndTime,
        )
        TimetableInputField(
            text = customContents.time.place,
            title = stringResource(id = R.string.timetable_input_field_title_place),
            onValueChange = onPlaceNameChange
        )
    }
}

@Preview
@Composable
private fun BottomSheetCustomContentPreview() {
    BottomSheetCustomContent(
        customContents = CustomContentState(),
    )
}