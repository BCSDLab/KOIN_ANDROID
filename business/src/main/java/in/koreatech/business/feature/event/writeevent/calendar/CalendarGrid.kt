package `in`.koreatech.business.feature.event.writeevent.calendar

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.event.writeevent.writeevent.WriteEventViewModel
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorPrimary400
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun CalendarScreen(
    viewModel: WriteEventViewModel = hiltViewModel(),
    selectedYearMonth: YearMonth = YearMonth.now(),
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                viewModel.onSelectedYearMonthChanged(selectedYearMonth.minusMonths(1))
            }) {
                Icon(painterResource(R.drawable.ic_arrow_left), contentDescription = "이전 달")
            }

            TextButton(onClick = {
                viewModel.onDatePickerVisibilityChanged(
                    true
                )
            }) {
                Text(
                    text = "${selectedYearMonth.year}년 ${selectedYearMonth.monthValue}월",
                    fontSize = 18.sp,
                    color = ColorPrimary
                )
            }

            IconButton(onClick = {
                viewModel.onSelectedYearMonthChanged(
                    selectedYearMonth.plusMonths(
                        1
                    )
                )
            }) {
                Icon(painterResource(R.drawable.ic_arrow_right), contentDescription = "다음 달")
            }
        }

        CalendarGrid(
            yearMonth = selectedYearMonth,
            startDate = null,
            endDate = null,
            onDateSelected = { startDate, endDate ->
                viewModel.onStartDateChanged(startDate?.toString() ?: "")
                viewModel.onEndDateChanged(endDate?.toString() ?: "")
            }
        )
    }
}


@Composable
fun CalendarGrid(
    yearMonth: YearMonth = YearMonth.now(),
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateSelected: (LocalDate?, LocalDate?) -> Unit
) {
    var selectedStartDate by remember { mutableStateOf(startDate) }
    var selectedEndDate by remember { mutableStateOf(endDate) }

    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { index, day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (index) {
                        0 -> Color.Red
                        6 -> Color.Blue
                        else -> Color.Black
                    },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(firstDayOfMonth) {
                Box(modifier = Modifier.size(40.dp))
            }

            items(daysInMonth) { day ->
                val date = yearMonth.atDay(day + 1)
                val dayOfWeek = date.dayOfWeek.value % 7
                val isInRange =
                    selectedStartDate != null && selectedEndDate != null && date in selectedStartDate!!..selectedEndDate!!

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                date == selectedStartDate -> ColorPrimary400
                                date == selectedEndDate -> ColorPrimary400
                                isInRange -> ColorPrimary400.copy(alpha = 0.5f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable {
                            when {
                                selectedStartDate == null -> {
                                    selectedStartDate = date
                                    onDateSelected(selectedStartDate, selectedStartDate)
                                }

                                selectedEndDate == null -> {
                                    if (date.isBefore(selectedStartDate)) {
                                        selectedEndDate = selectedStartDate
                                        selectedStartDate = date
                                    } else {
                                        selectedEndDate = date
                                    }
                                    onDateSelected(selectedStartDate, selectedEndDate)
                                }

                                else -> {
                                    selectedStartDate = date
                                    selectedEndDate = null
                                    onDateSelected(selectedStartDate, selectedStartDate)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 14.sp,
                        color = when {
                            date == selectedStartDate || date == selectedEndDate -> Color.White
                            isInRange -> Color.White
                            dayOfWeek == 6 -> Color.Blue
                            dayOfWeek == 0 -> Color.Red
                            else -> Color.Black
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCalendarGrid() {
    CalendarGrid(
        yearMonth = YearMonth.now(),
        startDate = LocalDate.now().minusDays(5),
        endDate = LocalDate.now().plusDays(5),
        onDateSelected = { _, _ -> }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewCalendarScreen() {
    CalendarScreen()
}