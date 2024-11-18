package `in`.koreatech.koin.feature.timetable.view.dialog

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.picker.KoinPicker
import `in`.koreatech.koin.core.designsystem.component.picker.rememberPickerState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import timber.log.Timber
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableTimePickerDialog(
    title: String,
    isStartTime: Boolean,
    customContent: CustomExtraContentState,
    modifier: Modifier = Modifier,
    onDismiss: (content: CustomExtraContentState, visible: Boolean) -> Unit = { _, _ -> },
    onConfirm: (customContent: CustomExtraContentState) -> Unit = { }
) {
    val hourItems = if (isStartTime) {
        (9..23).map { it.toString() }
    } else {
        (9..24).map { it.toString() }
    }
    val minutesItems = listOf("00", "30")

    val hourPickerState = rememberPickerState()
    val minutesPickerState = rememberPickerState()

    val hourItemsState = rememberSaveable { mutableStateOf(hourItems) }
    val minutesItemsState = rememberSaveable { mutableStateOf(minutesItems) }


    LaunchedEffect(hourPickerState) {
        snapshotFlow { hourPickerState.selectedItem }
            .collect { item ->
                if (isStartTime.not()) {
                    if (item == "9") {
                        minutesItemsState.value = listOf("30")
                    } else if (item == "24") {
                        minutesItemsState.value = listOf("00")
                    } else {
                        minutesItemsState.value = minutesItems
                    }
                }
            }
    }

    LaunchedEffect(minutesPickerState) {
        snapshotFlow { minutesPickerState.selectedItem }
            .collect { item ->
                Timber.e("item :$item")
            }
    }


    BasicAlertDialog(
        onDismissRequest = { onDismiss(customContent, false) },
        modifier = modifier
    ) {
        Surface(
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(
                        top = 24.dp,
                        bottom = 19.dp,
                        start = (31.5).dp,
                        end = (31.5).dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KoinPicker(
                        items = hourItemsState.value,
                        pickerState = hourPickerState,
                        visibleItemsCount = 5,
                        startIndex = if (isStartTime) {
                            (customContent.startTime.hour - 9)
                        } else {
                            if (customContent.endTime.hour == 23 && customContent.endTime.minute == 59) {
                                hourItems.size - 1
                            } else {
                                (customContent.endTime.hour - 9)
                            }
                        },
                        selectedTextStyle = KoinTheme.typography.bold20.copy(
                            color = KoinTheme.colors.neutral700,
                            textAlign = TextAlign.End
                        ),
                        unselectedTextStyle = KoinTheme.typography.bold20.copy(
                            color = KoinTheme.colors.neutral500,
                            textAlign = TextAlign.End
                        ),
                        brushVerticalGradient = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black,
                            1f to Color.Transparent
                        ),
                        modifier = Modifier.weight(.45f),
                    )
                    Text(
                        text = ":",
                        style = KoinTheme.typography.bold20,
                        color = KoinTheme.colors.neutral700,
                        modifier = Modifier.weight(.1f),
                        textAlign = TextAlign.Center
                    )
                    KoinPicker(
                        items = minutesItemsState.value,
                        pickerState = minutesPickerState,
                        visibleItemsCount = 5,
                        startIndex = if (isStartTime) {
                            if (customContent.startTime.minute == 0) {
                                0
                            } else {
                                1
                            }
                        } else {
                            if (customContent.endTime.minute == 0 || customContent.endTime.minute == 59) {
                                0
                            } else {
                                1
                            }
                        },
                        selectedTextStyle = KoinTheme.typography.bold20.copy(
                            color = KoinTheme.colors.neutral700
                        ),
                        unselectedTextStyle = KoinTheme.typography.bold20.copy(
                            color = KoinTheme.colors.neutral500
                        ),
                        modifier = Modifier.weight(.45f),
                        brushVerticalGradient = Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black,
                            1f to Color.Transparent
                        ),
                        infiniteScroll = false
                    )
                }
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        colors = ButtonColors(
                            containerColor = KoinTheme.colors.neutral0,
                            contentColor = KoinTheme.colors.neutral500,
                            disabledContainerColor = KoinTheme.colors.neutral400,
                            disabledContentColor = KoinTheme.colors.neutral500
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(1.dp, KoinTheme.colors.neutral500),
                        onClick = { onDismiss(customContent, false) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_cancellation),
                            style = KoinTheme.typography.medium15,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                    FilledTextButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.common_confirmation),
                        onClick = {
                            if (isStartTime) {
                                onConfirm(
                                    customContent.copy(
                                        startTime = LocalTime.of(
                                            hourPickerState.selectedItem.toInt(),
                                            minutesPickerState.selectedItem.toInt()
                                        )
                                    )
                                )
                            } else {
                                if (hourPickerState.selectedItem == "9") {
                                    onConfirm(
                                        customContent.copy(
                                            endTime = LocalTime.of(
                                                hourPickerState.selectedItem.toInt(),
                                                30
                                            )
                                        )
                                    )
                                } else if (hourPickerState.selectedItem == "24") {
                                    onConfirm(
                                        customContent.copy(
                                            endTime = LocalTime.of(23, 59)
                                        )
                                    )
                                } else {
                                    onConfirm(
                                        customContent.copy(
                                            endTime = LocalTime.of(
                                                hourPickerState.selectedItem.toInt(),
                                                minutesPickerState.selectedItem.toInt()
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 300)
@Composable
private fun TimetableTimePickerDialogPreview() {
    TimetableTimePickerDialog(
        title = "시작시간을 선택해주세요.",
        isStartTime = true,
        customContent = CustomExtraContentState(),
    )
}