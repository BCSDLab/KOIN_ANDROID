package `in`.koreatech.koin.feature.dining.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseTimeInfo
import `in`.koreatech.koin.feature.dining.R

@Composable
fun DiningTimetableItem(
    titleText: String,
    openCloseTimeInfoList: List<OpenCloseTimeInfo>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KoinTheme.colors.neutral0)
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 12.dp,
                bottom = 4.dp
            )
    ) {
        Text(
            text = titleText,
            style = KoinTheme.typography.bold18,
            color = KoinTheme.colors.primary500
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral400
        )
        DiningTimetableItemSheets(
            modifier = Modifier
                .background(
                    color = KoinTheme.colors.neutral50
                )
                .padding(vertical = 5.dp),
            firstText = stringResource(R.string.dining_notice_timetable_type),
            secondText = stringResource(R.string.dining_notice_timetable_open_time),
            thirdText = stringResource(R.string.dining_notice_timetable_close_time),
            style = KoinTheme.typography.medium14
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral400
        )
        Spacer(Modifier.height(2.dp))
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            openCloseTimeInfoList.forEachIndexed { index, it ->
                DiningTimetableItemSheets(
                    firstText = it.type,
                    secondText = it.openTime,
                    thirdText = it.closeTime,
                    style = KoinTheme.typography.regular16
                )
                if (index != openCloseTimeInfoList.lastIndex) {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = KoinTheme.colors.neutral300
                    )
                }
            }
        }
        Spacer(Modifier.height(2.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral400
        )
    }
}

@Composable
fun DiningTimetableItemSheets(
    modifier: Modifier = Modifier,
    firstText: String,
    secondText: String,
    thirdText: String,
    style: TextStyle = KoinTheme.typography.medium14,
    color: Color = KoinTheme.colors.neutral800
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = firstText,
            style = style,
            textAlign = TextAlign.Center,
            color = color
        )
        Text(
            modifier = Modifier.weight(1f),
            text = secondText,
            style = style,
            textAlign = TextAlign.Center,
            color = color
        )
        Text(
            modifier = Modifier.weight(1f),
            text = thirdText,
            style = style,
            textAlign = TextAlign.Center,
            color = color
        )
    }
}

@Preview
@Composable
fun DiningTimetableItemPreview() {
    DiningTimetableItem(
        titleText = "타이틀",
        openCloseTimeInfoList = listOf(
            OpenCloseTimeInfo(
                type = "아침",
                openTime = "08:00",
                closeTime = "09:00"
            ),
            OpenCloseTimeInfo(
                type = "점심",
                openTime = "13:00",
                closeTime = "14:00"
            ),
            OpenCloseTimeInfo(
                type = "저녁",
                openTime = "17:00",
                closeTime = "18:00"
            )
        )
    )
}
