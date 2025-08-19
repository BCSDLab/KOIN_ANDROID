package `in`.koreatech.koin.feature.dining.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import java.time.ZoneId
import java.util.Date

@Composable
fun DiningDateItem(
    date: Date,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Date) -> Unit = {}
) {
    val currentDate = TimeUtil.getCurrentTime()
    val isToday: Boolean = TimeUtil.getDateDifferenceInDays(currentDate, date) == 0
    val isBeforeDate: Boolean = date <= currentDate
    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(date) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = DateFormatUtil.getDayOfWeek(date),
            style = KoinTheme.typography.regular13,
            color = if (isBeforeDate) KoinTheme.colors.neutral500 else KoinTheme.colors.neutral800
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = if (isSelected) KoinTheme.colors.primary500 else KoinTheme.colors.neutral0,
                    shape = CircleShape
                ).then(
                    if (isToday) {
                        Modifier.border(1.dp, KoinTheme.colors.primary500, CircleShape)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth.toString(),
                style = KoinTheme.typography.medium15,
                color = if (isSelected) {
                    KoinTheme.colors.neutral0
                } else {
                    if (isToday) {
                        KoinTheme.colors.primary500
                    } else {
                        if (isBeforeDate) {
                            KoinTheme.colors.neutral500
                        } else {
                            KoinTheme.colors.neutral800
                        }
                    }
                }
            )
        }
        Spacer(Modifier.height(5.dp))
        Spacer(
            Modifier
                .size(12.dp, 2.dp)
                .border(
                    1.dp,
                    color = if (isToday) KoinTheme.colors.primary500 else KoinTheme.colors.neutral0,
                    shape = KoinTheme.shapes.extraLarge
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiningDateItemCurrentPreview() {
    DiningDateItem(
        date = TimeUtil.getCurrentTime(),
        isSelected = false
    )
}

@Preview(showBackground = true)
@Composable
fun DiningDateItemAfterPreview() {
    DiningDateItem(
        date = TimeUtil.getNextDayDate(TimeUtil.getCurrentTime()),
        isSelected = false
    )
}

@Preview(showBackground = true)
@Composable
fun DiningDateItemSeletedPreview() {
    DiningDateItem(
        date = TimeUtil.getCurrentTime(),
        isSelected = true
    )
}
