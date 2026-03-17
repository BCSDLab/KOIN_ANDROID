package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun CallvanDateField(
    formattedDate: String,
    isPickerVisible: Boolean,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    modifier: Modifier = Modifier,
    onFieldClick: () -> Unit = {},
    onYearChange: (Int) -> Unit = {},
    onMonthChange: (Int) -> Unit = {},
    onDayChange: (Int) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (isPickerVisible) 180f else 0f,
        label = "arrowRotation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CallvanSectionHeader(
                label = stringResource(R.string.callvan_create_date_label),
                hint = stringResource(R.string.callvan_create_date_hint)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RebrandKoinTheme.colors.neutral100, RebrandKoinTheme.shapes.small)
                    .clickable(onClick = onFieldClick)
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
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(arrowRotation)
                )
            }
        }
        AnimatedVisibility(
            visible = isPickerVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CallvanDatePickerCard(
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                selectedDay = selectedDay,
                onYearChange = onYearChange,
                onMonthChange = onMonthChange,
                onDayChange = onDayChange,
                onReset = onReset,
                onConfirm = onConfirm
            )
        }
    }
}

@Composable
private fun CallvanDatePickerCard(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    modifier: Modifier = Modifier,
    onYearChange: (Int) -> Unit = {},
    onMonthChange: (Int) -> Unit = {},
    onDayChange: (Int) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val today = remember { LocalDate.now() }
    val currentYear = remember { today.year }
    val currentMonth = remember { today.monthValue }
    val currentDay = remember { today.dayOfMonth }
    val years = remember { persistentListOf("${currentYear}년", "${currentYear + 1}년") }
    val startMonth = remember(selectedYear) {
        if (selectedYear == currentYear) currentMonth else 1
    }
    val months = remember(selectedYear) {
        (startMonth..12).map { "${it}월" }.toPersistentList()
    }
    val startDay = remember(selectedYear, selectedMonth) {
        if (selectedYear == currentYear && selectedMonth == currentMonth) currentDay else 1
    }
    val days = remember(selectedYear, selectedMonth) {
        val daysCount = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
        (startDay..daysCount).map { "${it}일" }.toPersistentList()
    }

    val yearIndex = remember(selectedYear, currentYear) {
        (selectedYear - currentYear).coerceIn(0, years.size - 1)
    }
    val monthIndex = remember(selectedMonth, startMonth) {
        (selectedMonth - startMonth).coerceIn(0, months.size - 1)
    }
    val dayIndex = remember(selectedDay, startDay, days.size) {
        (selectedDay - startDay).coerceIn(0, days.size - 1)
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
                        onYearChange(newYear)
                        val newStartDay = if (newYear == currentYear && selectedMonth == currentMonth) currentDay else 1
                        if (selectedDay < newStartDay) onDayChange(newStartDay)
                    },
                    modifier = Modifier.weight(1f)
                )
                CallvanScrollPicker(
                    items = months,
                    selectedIndex = monthIndex,
                    onIndexChange = { index ->
                        val newMonth = startMonth + index
                        onMonthChange(newMonth)
                        val newStartDay = if (selectedYear == currentYear && newMonth == currentMonth) currentDay else 1
                        if (selectedDay < newStartDay) onDayChange(newStartDay)
                    },
                    modifier = Modifier.weight(1f)
                )
                CallvanScrollPicker(
                    items = days,
                    selectedIndex = dayIndex,
                    onIndexChange = { index -> onDayChange(startDay + index) },
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
            ) {
                Text(
                    text = stringResource(R.string.callvan_create_picker_reset),
                    style = RebrandKoinTheme.typography.medium14,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.clickable(onClick = onReset)
                )
                Text(
                    text = stringResource(R.string.callvan_create_picker_confirm),
                    style = RebrandKoinTheme.typography.medium14,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.clickable(onClick = onConfirm)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanDateFieldPreview() {
    CallvanDateField(
        formattedDate = "2026년 03월 07일",
        isPickerVisible = false,
        selectedYear = 2026,
        selectedMonth = 3,
        selectedDay = 7,
        onFieldClick = {},
        onYearChange = {},
        onMonthChange = {},
        onDayChange = {},
        onReset = {},
        onConfirm = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanDateFieldPickerVisiblePreview() {
    CallvanDateField(
        formattedDate = "2026년 03월 07일",
        isPickerVisible = true,
        selectedYear = 2026,
        selectedMonth = 3,
        selectedDay = 7,
        onFieldClick = {},
        onYearChange = {},
        onMonthChange = {},
        onDayChange = {},
        onReset = {},
        onConfirm = {}
    )
}
