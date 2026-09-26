package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState

@Composable
fun TimetableTimeContentRow(
    customContent: CustomExtraContentState,
    modifier: Modifier = Modifier,
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { },
    onClickStartTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onClickEndTime: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> }
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.timetable_input_field_option_character),
            style = RebrandKoinTheme.typography.regular16,
            color = RebrandKoinTheme.colors.primary600,
            modifier = Modifier.padding(end = 1.dp, bottom = 1.dp)
        )
        Text(
            text = stringResource(id = R.string.timetable_input_field_title_time),
            style = RebrandKoinTheme.typography.bold12,
            color = RebrandKoinTheme.colors.neutral800
        )

        Spacer(modifier = Modifier.weight(1f))

        DayOfWeekEditBox(
            customContent = customContent,
            onDayOfWeekChange = onDayOfWeekChange
        )
        Spacer(modifier = Modifier.width(11.dp))
        TimeEditBox(
            content = customContent,
            localTime = customContent.startTime,
            onClick = onClickStartTime
        )
        Spacer(modifier = Modifier.width(11.dp))
        Text(
            text = stringResource(id = R.string.timetable_input_field_wave_character),
            style = RebrandKoinTheme.typography.medium18,
            color = RebrandKoinTheme.colors.neutral800
        )
        Spacer(modifier = Modifier.width(11.dp))
        TimeEditBox(
            content = customContent,
            localTime = customContent.endTime,
            onClick = onClickEndTime
        )
    }
}

@Preview
@Composable
private fun TimetableTimeContentRowPreview() {
    TimetableTimeContentRow(
        customContent = CustomExtraContentState()
    )
}
