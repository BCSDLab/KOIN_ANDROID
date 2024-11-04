package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableTimeContentRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.timetable_input_field_option_character),
                style = KoinTheme.typography.regular16,
                color = KoinTheme.colors.sub500,
                modifier = Modifier.padding(end = 1.dp, bottom = 1.dp)
            )
            Text(
                text = stringResource(id = R.string.timetable_input_field_title_time),
                style = KoinTheme.typography.bold12,
                color = KoinTheme.colors.neutral800
            )
        }

        Row(modifier = Modifier) {
            TimeEditBox(
                text = "월요일"
            )
            Spacer(modifier = Modifier.width(11.dp))
            TimeEditBox(
                text = "09:00"
            )
            Spacer(modifier = Modifier.width(11.dp))
            Text(
                text = stringResource(id = R.string.timetable_input_field_wave_character),
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800
            )
            Spacer(modifier = Modifier.width(11.dp))
            TimeEditBox(
                text = "11:00"
            )
        }
    }
}

@Preview
@Composable
private fun TimetableTimeContentRowPreview() {
    TimetableTimeContentRow()
}