package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private fun to24Hour(isAm: Boolean, displayHour: Int): Int = when {
    isAm -> displayHour
    else -> displayHour + 12
}

private fun toDisplayHour(hour24: Int): Int = when {
    hour24 <= 12 -> hour24
    else -> hour24 - 12
}

@Composable
fun CallvanTimeField(
    isPickerVisible: Boolean,
    selectedDate: LocalDate,
    selectedTime: LocalTime,
    onFieldClick: () -> Unit = {},
    onTimeChange: (LocalTime) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val isAm = remember(selectedTime) { selectedTime.hour <= 12 }
    val displayHour = remember(selectedTime) { toDisplayHour(selectedTime.hour) }
    val amPmText = stringResource(if (isAm) R.string.callvan_am else R.string.callvan_pm)
    val formattedTime = remember(selectedTime) {
        "%02d:%02d".format(displayHour, selectedTime.minute)
    }
    var popupOffsetHeightPx by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CallvanSectionHeader(
            label = stringResource(R.string.callvan_create_time_label),
            hint = stringResource(R.string.callvan_create_time_hint)
        )
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RebrandKoinTheme.colors.neutral400, RebrandKoinTheme.shapes.extraSmall)
                    .clip(RebrandKoinTheme.shapes.extraSmall)
                    .clickable(onClick = onFieldClick)
                    .onGloballyPositioned { popupOffsetHeightPx = it.size.height + 20 },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amPmText,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    color = RebrandKoinTheme.colors.neutral400
                )
                Text(
                    text = formattedTime,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            if (isPickerVisible) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = popupOffsetHeightPx),
                    onDismissRequest = onFieldClick,
                    properties = PopupProperties(focusable = true)
                ) {
                    CallvanTimePickerCard(
                        selectedDate = selectedDate,
                        selectedTime = selectedTime,
                        onTimeChange = onTimeChange,
                        onReset = onReset,
                        onConfirm = onConfirm
                    )
                }
            }
        }
    }
}

@Composable
private fun CallvanTimePickerCard(
    selectedDate: LocalDate,
    selectedTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    onReset: () -> Unit,
    onConfirm: () -> Unit
) {
    val amLabel = stringResource(R.string.callvan_am)
    val pmLabel = stringResource(R.string.callvan_pm)

    val isToday = remember(selectedDate) { selectedDate == LocalDate.now() }
    val now = LocalTime.now()
    val currentThreshold = remember(now.hour, now.minute, isToday) { if (isToday) now.hour * 60 + now.minute else 0 }

    val isAm = remember(now.hour) { now.hour <= 12 }
    val selectedHour = remember(selectedTime.hour) { toDisplayHour(selectedTime.hour) }

    val amAvailable = !isToday || currentThreshold <= 779

    val amPmItems = remember(amLabel, pmLabel, amAvailable) {
        if (amAvailable) persistentListOf(amLabel, pmLabel) else persistentListOf(pmLabel)
    }

    val hourItems = remember(isAm, currentThreshold) {
        val range = if (isAm) (0..12) else (1..11)
        range.filter { hour ->
            to24Hour(isAm, hour) * 60 + 59 >= currentThreshold
        }.toImmutableList()
    }

    val minMinute = if (isToday) {
        maxOf(currentThreshold - selectedTime.hour * 60, 0)
    } else {
        0
    }
    val minuteItems = remember(minMinute) {
        (minMinute..59).toImmutableList()
    }

    val amPmIndex = remember(isAm, amAvailable) {
        if (!amAvailable) 0 else if (isAm) 0 else 1
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = RebrandKoinTheme.colors.neutral100),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CallvanStringScrollPicker(
                    items = amPmItems,
                    selectedIndex = amPmIndex,
                    onIndexChange = { index ->
                        val newIsAm = if (amAvailable) index == 0 else false
                        val clampedHour = if (newIsAm) selectedHour.coerceIn(0, 12) else selectedHour.coerceIn(1, 11)
                        val newHour24 = to24Hour(newIsAm, clampedHour)
                        onTimeChange(LocalTime.of(newHour24, selectedTime.minute))
                    },
                    modifier = Modifier.weight(1f)
                )
                CallvanIntScrollPicker(
                    items = hourItems,
                    selectedValue = selectedHour,
                    suffix = "",
                    onValueChange = { newDisplayHour ->
                        val newHour24 = to24Hour(isAm, newDisplayHour)
                        onTimeChange(LocalTime.of(newHour24, selectedTime.minute))
                    },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                CallvanIntScrollPicker(
                    items = minuteItems,
                    selectedValue = selectedTime.minute,
                    suffix = "",
                    onValueChange = { newMinute ->
                        onTimeChange(LocalTime.of(selectedTime.hour, newMinute))
                    },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            CallvanPickerFooter(onReset = onReset, onConfirm = onConfirm)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanTimeFieldPreview() {
    CallvanTimeField(
        isPickerVisible = false,
        selectedDate = LocalDate.now(),
        selectedTime = LocalTime.of(9, 30),
        onFieldClick = {},
        onTimeChange = {},
        onReset = {},
        onConfirm = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanTimeFieldPickerVisiblePreview() {
    CallvanTimeField(
        isPickerVisible = true,
        selectedDate = LocalDate.now(),
        selectedTime = LocalTime.of(14, 45),
        onFieldClick = {},
        onTimeChange = {},
        onReset = {},
        onConfirm = {}
    )
}
