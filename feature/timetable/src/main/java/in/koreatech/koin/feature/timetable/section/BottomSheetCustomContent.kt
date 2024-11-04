package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.TimetableInputField
import `in`.koreatech.koin.feature.timetable.component.TimetableTimeContentRow

@Composable
fun BottomSheetCustomContent(
    modifier: Modifier = Modifier,
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onPlaceNameChange: (text: String) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimetableInputField(
            title = stringResource(id = R.string.timetable_input_field_title_schedule),
            optional = true,
            onValueChange = onScheduleNameChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimetableInputField(
            title = stringResource(id = R.string.timetable_input_field_title_professor),
            onValueChange = onProfessorNameChange
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimetableTimeContentRow()
        Spacer(modifier = Modifier.height(8.dp))
        TimetableInputField(
            title =  stringResource(id = R.string.timetable_input_field_title_place),
            onValueChange = onPlaceNameChange
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun BottomSheetCustomContentPreview() {
    BottomSheetCustomContent()
}