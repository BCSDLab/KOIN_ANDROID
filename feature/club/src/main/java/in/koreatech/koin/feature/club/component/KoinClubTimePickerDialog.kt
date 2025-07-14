package `in`.koreatech.koin.feature.club.component

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
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.picker.KoinPicker
import `in`.koreatech.koin.core.designsystem.component.picker.rememberPickerState
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import java.time.LocalTime
import timber.log.Timber

/**
 * Similar component at Timetable module's TimePickerDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubTimePickerDialog(
    title: String,
    isStartTime: Boolean,
    defaultTime: LocalTime,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onPositive: (LocalTime) -> Unit = {},
    onNegative: () -> Unit = {}
) {
    val hourItems = (0..23).map { it.toString() }
    val minutesItems = (0..59).map { String.format("%02d", it) }

    val hourPickerState = rememberPickerState()
    val minutesPickerState = rememberPickerState()

    val hourItemsState = rememberSaveable { mutableStateOf(hourItems) }
    val minutesItemsState = rememberSaveable { mutableStateOf(minutesItems) }

    LaunchedEffect(hourPickerState) {
        snapshotFlow { hourPickerState.selectedItem }
            .collect { item ->
                if (isStartTime.not()) {
                    if (item == "24") {
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
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Surface(
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier =
                Modifier
                    .background(color = Color.White)
                    .padding(
                        top = 24.dp,
                        bottom = 19.dp,
                        start = (31.5).dp,
                        end = (31.5).dp
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
                    modifier =
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KoinPicker(
                        items = hourItemsState.value,
                        pickerState = hourPickerState,
                        visibleItemsCount = 5,
                        startIndex = defaultTime.hour,
                        selectedTextStyle =
                        KoinTheme.typography.bold20.copy(
                            textAlign = TextAlign.End
                        ),
                        unselectedTextStyle =
                        KoinTheme.typography.bold20.copy(
                            textAlign = TextAlign.End
                        ),
                        selectedItemColor = KoinTheme.colors.neutral700,
                        unselectedItemColor = KoinTheme.colors.neutral500,
                        brushVerticalGradient =
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Black,
                            1f to Color.Transparent
                        ),
                        modifier = Modifier.weight(.45f)
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
                        startIndex = defaultTime.minute,
                        selectedTextStyle = KoinTheme.typography.bold20,
                        unselectedTextStyle = KoinTheme.typography.bold20,
                        selectedItemColor = KoinTheme.colors.neutral700,
                        unselectedItemColor = KoinTheme.colors.neutral500,
                        modifier = Modifier.weight(.45f),
                        brushVerticalGradient =
                        Brush.verticalGradient(
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
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        colors =
                        ButtonColors(
                            containerColor = KoinTheme.colors.neutral0,
                            contentColor = KoinTheme.colors.neutral500,
                            disabledContainerColor = KoinTheme.colors.neutral400,
                            disabledContentColor = KoinTheme.colors.neutral500
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(0.dp),
                        border = BorderStroke(1.dp, KoinTheme.colors.neutral500),
                        onClick = onNegative
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_cancellation),
                            style = KoinTheme.typography.medium15,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                    FilledButton(
                        modifier =
                        Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.common_confirmation),
                        onClick = {
                            onPositive(
                                LocalTime.of(
                                    hourPickerState.selectedItemIndex,
                                    minutesPickerState.selectedItemIndex
                                )
                            )
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
    KoinClubTimePickerDialog(
        title = "시작시간을 선택해주세요.",
        isStartTime = true,
        defaultTime = LocalTime.now()
    )
}
