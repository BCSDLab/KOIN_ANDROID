package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.TimetableInputField
import `in`.koreatech.koin.feature.timetable.component.TimetableTimeContentRow
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun BottomSheetCustomExtraContent(
    customContent: CustomExtraContentState,
    modifier: Modifier = Modifier,
    onClickCancel: (id: Int) -> Unit = {},
    onPlaceNameChange: (id: Int, text: String) -> Unit,
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { },
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> }
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            IconButton(
                onClick = { onClickCancel(customContent.id) },
                modifier =
                Modifier
                    .padding(5.dp)
                    .size(24.dp)
            ) {
                StableIcon(
                    drawableResId = R.drawable.ic_bottomsheet_close
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 13.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            TimetableTimeContentRow(
                customContent = customContent,
                onDayOfWeekChange = onDayOfWeekChange,
                onClickStartTime = onClickStartTime,
                onClickEndTime = onClickEndTime
            )
            if (customContent.isError) {
                Text(
                    text = stringResource(R.string.timetable_error_input_field_duplication_time),
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.sub500,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            TimetableInputField(
                text = customContent.place,
                title = stringResource(id = R.string.timetable_input_field_title_place),
                onValueChange = {
                    onPlaceNameChange(customContent.id, it)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetCustomExtraContentPreview() {
    BottomSheetCustomExtraContent(
        customContent = CustomExtraContentState(),
        modifier = Modifier.padding(10.dp),
        onPlaceNameChange = { _, _ -> }
    )
}
