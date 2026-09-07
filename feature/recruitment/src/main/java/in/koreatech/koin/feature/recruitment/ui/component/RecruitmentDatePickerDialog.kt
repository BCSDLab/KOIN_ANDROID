package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
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
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.model.toStable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDatePickerDialog(
    defaultDate: StableLocalDate,
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = defaultDate.value.toEpochMillisUtc()
    ),
    onDismiss: () -> Unit = {},
    onPositive: (StableLocalDate) -> Unit = {},
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
                    containerColor = RebrandKoinTheme.colors.neutral0,
                    titleContentColor = RebrandKoinTheme.colors.neutral800,
                    headlineContentColor = RebrandKoinTheme.colors.neutral800,
                    weekdayContentColor = RebrandKoinTheme.colors.neutral500,
                    subheadContentColor = RebrandKoinTheme.colors.neutral800,
                    yearContentColor = RebrandKoinTheme.colors.neutral800,
                    currentYearContentColor = RebrandKoinTheme.colors.primary500,
                    selectedYearContentColor = RebrandKoinTheme.colors.neutral0,
                    selectedYearContainerColor = RebrandKoinTheme.colors.primary500,
                    dayContentColor = RebrandKoinTheme.colors.neutral800,
                    selectedDayContentColor = RebrandKoinTheme.colors.neutral0,
                    selectedDayContainerColor = RebrandKoinTheme.colors.primary500,
                    todayContentColor = RebrandKoinTheme.colors.primary500,
                    todayDateBorderColor = RebrandKoinTheme.colors.primary500
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    text = stringResource(R.string.recruitment_date_picker_cancel),
                    onClick = onNegative,
                    modifier = Modifier.weight(1f),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        contentColor = RebrandKoinTheme.colors.primary500,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                        disabledContentColor = RebrandKoinTheme.colors.neutral500
                    ),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500)
                )
                FilledButton(
                    text = stringResource(R.string.recruitment_date_picker_confirm),
                    onClick = {
                        val date = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        } ?: LocalDate.now()
                        onPositive(date.toStable())
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    )
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
        RecruitmentDatePickerDialog(defaultDate = StableLocalDate.now())
    }
}
