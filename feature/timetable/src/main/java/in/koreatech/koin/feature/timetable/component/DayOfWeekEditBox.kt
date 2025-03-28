package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.model.toKorean
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import java.time.DayOfWeek

@Composable
fun DayOfWeekEditBox(
    customContent: CustomExtraContentState,
    modifier: Modifier = Modifier,
    onDayOfWeekChange: (content: CustomExtraContentState) -> Unit = { }
) {
    val days =
        arrayOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        )

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            modifier =
            Modifier
                .width(74.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = KoinTheme.colors.neutral300,
                    shape = RoundedCornerShape(4.dp)
                )
                .align(alignment = Alignment.Center)
        ) {
            days.forEachIndexed { index, day ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = day.toKorean(),
                            style = KoinTheme.typography.bold12,
                            color = KoinTheme.colors.neutral800,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        isExpanded = false
                        onDayOfWeekChange(customContent.copy(dayOfWeek = days[index]))
                    },
                    modifier =
                    Modifier
                        .height(35.dp)
                        .background(Color.White)
                        .drawBehind {
                            if (index != (days.size - 1)) {
                                drawLine(
                                    color = Color(0xFFE1E1E1),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                        }
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
        Row(
            modifier =
            Modifier
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = if (customContent.isError) KoinTheme.colors.sub500 else KoinTheme.colors.neutral300,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding((5.5).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = customContent.dayOfWeek.toKorean(),
                style = KoinTheme.typography.bold12,
                color = KoinTheme.colors.neutral800,
                modifier = Modifier.padding(start = (3.5).dp)
            )

            IconButton(
                onClick = {
                    isExpanded = true
                },
                modifier = Modifier.size(24.dp)
            ) {
                StableIcon(
                    drawableResId = R.drawable.ic_arrow_down
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DayOfWeekEditBoxEditBoxPreview() {
    DayOfWeekEditBox(
        customContent = CustomExtraContentState()
    )
}
