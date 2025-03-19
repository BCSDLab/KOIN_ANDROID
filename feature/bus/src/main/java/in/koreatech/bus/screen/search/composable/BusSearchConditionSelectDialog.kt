package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.picker.KoinPicker
import `in`.koreatech.koin.core.designsystem.component.picker.rememberPickerState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusSearchConditionSelectDialog(
    onDismissRequest: () -> Unit,
    onDepartureNow: () -> Unit,
    onComplete: (dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) -> Unit,
    dateList: ImmutableList<String>,
    daytimeList: ImmutableList<String>,
    hourList: ImmutableList<String>,
    minuteList: ImmutableList<String>,
    selectedDateIndex: Int,
    selectedDaytimeIndex: Int,
    selectedHourIndex: Int,
    selectedMinuteIndex: Int,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberPickerState()
    val daytimePickerState = rememberPickerState()
    val hourPickerState = rememberPickerState()
    val minutePickerState = rememberPickerState()

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest
    ) {
        Column {
            Text(
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                text = stringResource(R.string.set_departure_time),
                style = KoinTheme.typography.medium18
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp),
                text = stringResource(R.string.caution_timetable_period),
                style = KoinTheme.typography.regular14
            )
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(KoinTheme.colors.neutral50)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                KoinPicker(
                    items = dateList,
                    pickerState = datePickerState,
                    startIndex = selectedDateIndex,
                    visibleItemsCount = VISIBLE_ITEMS_COUNT,
                    infiniteScroll = false,
                    contentPadding = PaddingValues(vertical = 3.dp),
                    selectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    unselectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(.45f)
                )
                KoinPicker(
                    items = daytimeList,
                    pickerState = daytimePickerState,
                    startIndex = selectedDaytimeIndex,
                    visibleItemsCount = VISIBLE_ITEMS_COUNT,
                    infiniteScroll = false,
                    contentPadding = PaddingValues(top = 3.dp, bottom = 3.dp, start = 18.dp),
                    selectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    unselectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(.25f)
                )
                KoinPicker(
                    items = hourList,
                    pickerState = hourPickerState,
                    startIndex = selectedHourIndex,
                    visibleItemsCount = VISIBLE_ITEMS_COUNT,
                    infiniteScroll = true,
                    contentPadding = PaddingValues(top = 3.dp, bottom = 3.dp, start = 10.dp),
                    selectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    unselectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(.2f)
                )
                KoinPicker(
                    items = minuteList,
                    pickerState = minutePickerState,
                    startIndex = selectedMinuteIndex,
                    visibleItemsCount = VISIBLE_ITEMS_COUNT,
                    infiniteScroll = true,
                    contentPadding = PaddingValues(top = 3.dp, bottom = 3.dp, start = 10.dp),
                    selectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    unselectedTextStyle =
                    KoinTheme.typography.medium16.copy(
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.weight(.2f)
                )
            }
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledButton(
                    text = stringResource(R.string.departure_now),
                    onClick = onDepartureNow,
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = KoinTheme.colors.neutral600
                    ),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                )
                FilledButton(
                    text = stringResource(R.string.complete),
                    onClick = {
                        onComplete(
                            datePickerState.selectedItemIndex,
                            daytimePickerState.selectedItemIndex,
                            hourPickerState.selectedItemIndex,
                            minutePickerState.selectedItemIndex
                        )
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                )
            }
        }
    }
}

private const val VISIBLE_ITEMS_COUNT = 3
