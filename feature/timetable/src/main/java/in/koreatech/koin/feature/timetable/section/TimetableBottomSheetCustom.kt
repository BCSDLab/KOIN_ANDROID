package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.timetable.component.TimetableCustomAddBox
import `in`.koreatech.koin.feature.timetable.view.CustomEventExtraData

@Composable
fun TimetableBottomSheetCustom(
    modifier: Modifier = Modifier
) {
    val events: SnapshotStateList<CustomEventExtraData> = remember { mutableStateListOf() }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            BottomSheetCustomContent(
                onScheduleNameChange = {}, // TODO : 일정명 추가
                onProfessorNameChange = {}, // TODO : 교수명 추가
                onPlaceNameChange = {} // TODO : 장소 추가
            )
        }
        items(events.size, key = { events[it].position }) { index ->
            BottomSheetCustomExtraContent(
                position = events[index].position,
                onClickCancel = { cancelPosition ->
                    events.removeIf { it.position == cancelPosition }
                },
                onPlaceNameChange = { } // TODO : 장소 추가
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            TimetableCustomAddBox(
                onClick = {
                    if (events.isEmpty()) {
                        events.add(CustomEventExtraData())
                    } else {
                        events.add(CustomEventExtraData(position = events.last().position + 1))
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableBottomSheetCustomPreview() {
    TimetableBottomSheetCustom()
}

