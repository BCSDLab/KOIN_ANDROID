package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.TimetableCustomAddBox
import `in`.koreatech.koin.feature.timetable.component.TimetableInputField
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun TimetableBottomSheetCustom(
    customContents: CustomContentState,
    sheetLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onExtraPlaceNameChange: (id: Int, text: String) -> Unit = { _, _ -> },
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { },
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickAddCustomContent: () -> Unit = {},
    onClickRemoveCustomContent: (id: Int) -> Unit = {},
) {
    val nestedScroll = rememberNestedScrollInteropConnection()
    LazyColumn(
        modifier = modifier
            .nestedScroll(nestedScroll),
        state = sheetLazyListState,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            TimetableInputField(
                text = customContents.schedule,
                title = stringResource(id = R.string.timetable_input_field_title_schedule),
                optional = false,
                isError = customContents.isScheduleError,
                onValueChange = onScheduleNameChange
            )
            Spacer(modifier = Modifier.height(8.dp))
            TimetableInputField(
                text = customContents.professor,
                title = stringResource(id = R.string.timetable_input_field_title_professor),
                onValueChange = onProfessorNameChange
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (customContents.data.size == 1) {
            item {
                BottomSheetCustomContent(
                    customExtraContentState = customContents.data.first(),
                    onPlaceNameChange = onExtraPlaceNameChange,
                    onDayOfWeekChange = onDayOfWeekChange,
                    onClickStartTime = onClickStartTime,
                    onClickEndTime = onClickEndTime,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            items(customContents.data.size, key = { customContents.data[it].id }) { index ->
                BottomSheetCustomExtraContent(
                    customContent = customContents.data[index],
                    onClickCancel = onClickRemoveCustomContent,
                    onPlaceNameChange = onExtraPlaceNameChange,
                    onDayOfWeekChange = onDayOfWeekChange,
                    onClickStartTime = onClickStartTime,
                    onClickEndTime = onClickEndTime,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            TimetableCustomAddBox(
                onClick = onClickAddCustomContent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableBottomSheetCustomPreview() {
    TimetableBottomSheetCustom(
        customContents = CustomContentState(),
        sheetLazyListState = rememberLazyListState()
    )
}

