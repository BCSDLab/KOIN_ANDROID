package `in`.koreatech.koin.feature.club.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubDatePickerDialog(
    defaultDate: LocalDate,
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState = rememberDatePickerState(initialSelectedDateMillis = defaultDate.toMillis()),
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
                    color = KoinTheme.colors.neutral0,
                    shape = KoinTheme.shapes.large
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
        ) {
            DatePicker(
                modifier = Modifier.fillMaxWidth(),
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = KoinTheme.colors.neutral0
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    text = stringResource(R.string.club_dialog_cancel),
                    onClick = onNegative,
                    modifier = Modifier
                        .weight(1f)
                )
                FilledButton(
                    text = stringResource(R.string.club_dialog_ok),
                    onClick = {
                        val date = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        } ?: LocalDate.now()
                        onPositive(date)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

fun getDayOfWeek(dayOfWeek: DayOfWeek): String =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }

fun LocalDate.toMillis(): Long =
    this.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun KoinClubDatePickerDialogPreView() {
    KoinTheme {
        KoinClubDatePickerDialog(
            defaultDate = LocalDate.now()
        )
    }
}
