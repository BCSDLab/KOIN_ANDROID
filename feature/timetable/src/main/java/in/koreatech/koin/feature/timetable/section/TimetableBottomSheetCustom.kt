package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.TimetableCustomAddBox
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun TimetableBottomSheetCustom(
    customContents: CustomContentState,
    modifier: Modifier = Modifier,
    onScheduleNameChange: (text: String) -> Unit = {},
    onProfessorNameChange: (text: String) -> Unit = {},
    onPlaceNameChange: (text: String) -> Unit = {},
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
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            BottomSheetCustomContent(
                customContents = customContents,
                onScheduleNameChange = onScheduleNameChange, // TODO : 일정명 추가
                onProfessorNameChange = onProfessorNameChange, // TODO : 교수명 추가
                onPlaceNameChange = onPlaceNameChange, // TODO : 장소 추가
                onDayOfWeekChange = onDayOfWeekChange,
                onClickStartTime = onClickStartTime,
                onClickEndTime = onClickEndTime,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(customContents.data.size, key = { customContents.data[it].id }) { index ->
            BottomSheetCustomExtraContent(
                customContent = customContents.data[index],
                onClickCancel = onClickRemoveCustomContent,
                onPlaceNameChange = onExtraPlaceNameChange, // TODO : 장소 추가
                onDayOfWeekChange = onDayOfWeekChange,
                onClickStartTime = onClickStartTime,
                onClickEndTime = onClickEndTime,
            )
            Spacer(modifier = Modifier.height(8.dp))
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
    )
}

