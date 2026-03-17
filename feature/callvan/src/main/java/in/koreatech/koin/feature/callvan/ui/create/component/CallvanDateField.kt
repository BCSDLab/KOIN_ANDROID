package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun CallvanDateField(
    formattedDate: String,
    isPickerVisible: Boolean,
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    onFieldClick: () -> Unit = {},
    onDateChange: (LocalDate) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (isPickerVisible) 180f else 0f,
        label = "arrowRotation"
    )
    var popupOffestHeightPx by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CallvanSectionHeader(
            label = stringResource(R.string.callvan_create_date_label),
            hint = stringResource(R.string.callvan_create_date_hint)
        )
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RebrandKoinTheme.colors.neutral100, RebrandKoinTheme.shapes.small)
                    .clickable(onClick = onFieldClick)
                    .onGloballyPositioned { popupOffestHeightPx = it.size.height + 20 }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(arrowRotation)
                )
            }
            if (isPickerVisible) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = popupOffestHeightPx),
                    onDismissRequest = onFieldClick,
                    properties = PopupProperties(focusable = true)
                ) {
                    CallvanDatePickerCard(
                        selectedDate = selectedDate,
                        onDateChange = onDateChange,
                        onReset = onReset,
                        onConfirm = onConfirm
                    )
                }
            }
        }
    }
}

@Composable
private fun CallvanDatePickerCard(
    selectedDate: LocalDate,
    modifier: Modifier = Modifier,
    onDateChange: (LocalDate) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val today = remember { LocalDate.now() }
    val currentYear = today.year
    val currentMonth = today.monthValue
    val currentDay = today.dayOfMonth

    val selectedYear = selectedDate.year
    val selectedMonth = selectedDate.monthValue
    val selectedDay = selectedDate.dayOfMonth

    val isCurrentYear = selectedYear == currentYear
    val isCurrentMonth = isCurrentYear && selectedMonth == currentMonth

    val years = remember { persistentListOf("${currentYear}년", "${currentYear + 1}년") }

    val minMonth = if (isCurrentYear) currentMonth else 1
    val months: ImmutableList<String> = remember(minMonth) {
        (minMonth..12).map { "${it}월" }.toPersistentList()
    }

    val daysInMonth = remember(selectedYear, selectedMonth) {
        YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    }
    val minDay = if (isCurrentMonth) currentDay else 1
    val days: ImmutableList<String> = remember(selectedYear, selectedMonth, minDay) {
        (minDay..daysInMonth).map { "${it}일" }.toPersistentList()
    }

    val yearIndex = remember(selectedYear) {
        (selectedYear - currentYear).coerceIn(0, years.size - 1)
    }
    val monthIndex = remember(selectedMonth, minMonth) {
        (selectedMonth - minMonth).coerceIn(0, months.size - 1)
    }
    val dayIndex = remember(selectedDay, minDay, days.size) {
        (selectedDay - minDay).coerceIn(0, days.size - 1)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RebrandKoinTheme.shapes.small,
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
                CallvanScrollPicker(
                    items = years,
                    selectedIndex = yearIndex,
                    onIndexChange = { index ->
                        val newYear = currentYear + index
                        val safeDay = selectedDay.coerceAtMost(YearMonth.of(newYear, selectedMonth).lengthOfMonth())
                        onDateChange(LocalDate.of(newYear, selectedMonth, safeDay))
                    },
                    modifier = Modifier.weight(1f)
                )
                CallvanScrollPicker(
                    items = months,
                    selectedIndex = monthIndex,
                    onIndexChange = { index ->
                        val newMonth = minMonth + index
                        val safeDay = selectedDay.coerceAtMost(YearMonth.of(selectedYear, newMonth).lengthOfMonth())
                        onDateChange(LocalDate.of(selectedYear, newMonth, safeDay))
                    },
                    modifier = Modifier.weight(1f)
                )
                CallvanScrollPicker(
                    items = days,
                    selectedIndex = dayIndex,
                    onIndexChange = { index ->
                        onDateChange(LocalDate.of(selectedYear, selectedMonth, minDay + index))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            CallvanPickerFooter(onReset = onReset, onConfirm = onConfirm)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanDateFieldPreview() {
    CallvanDateField(
        formattedDate = "2026년 03월 07일",
        isPickerVisible = false,
        selectedDate = LocalDate.of(2026, 3, 7)
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanDateFieldPickerVisiblePreview() {
    CallvanDateField(
        formattedDate = "2026년 03월 07일",
        isPickerVisible = true,
        selectedDate = LocalDate.of(2026, 3, 7)
    )
}
