package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDatePickerDialog(
    defaultDate: LocalDate,
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = defaultDate.toEpochMillisUtc()
    ),
    onDismiss: () -> Unit = {},
    onPositive: (LocalDate) -> Unit = {},
    onNegative: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier = modifier.wrapContentWidth(unbounded = true).width(380.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = RebrandKoinTheme.colors.neutral0,
                    shape = RebrandKoinTheme.shapes.large
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            DatePicker(
                modifier = Modifier.fillMaxWidth(),
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = RebrandKoinTheme.colors.neutral0
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    text = "취소",
                    onClick = onNegative,
                    modifier = Modifier.weight(1f)
                )
                FilledButton(
                    text = "확인",
                    onClick = {
                        val date = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        } ?: LocalDate.now()
                        onPositive(date)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun LocalDate.toEpochMillisUtc(): Long =
    this.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RecruitmentDatePickerDialogPreview() {
    RebrandKoinTheme {
        RecruitmentDatePickerDialog(defaultDate = LocalDate.now())
    }
}
