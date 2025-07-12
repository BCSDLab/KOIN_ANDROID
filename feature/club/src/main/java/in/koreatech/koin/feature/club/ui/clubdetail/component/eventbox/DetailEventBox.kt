package `in`.koreatech.koin.feature.club.ui.clubdetail.component.eventbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DetailEventBox (
    eventName: String,
    stateText: String,
    dateText: String,
    eventIntroText: String,
    stateColor: Color,
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    onClick: () -> Unit = {}
) {
    Row (
        modifier = Modifier
            .border(
                width = 1.dp,
                color = KoinTheme.colors.primary300,
                shape = KoinTheme.shapes.small
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .width(100.dp)
                .aspectRatio(5f / 4f),
            loading = {
                CircularProgressIndicator()
            }
        )
        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text (
                    text = eventName,
                    style = KoinTheme.typography.bold16,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .background(color = stateColor, shape = KoinTheme.shapes.extraLarge)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stateText,
                        style = KoinTheme.typography.regular10.copy(fontSize = 11.sp),
                        color = KoinTheme.colors.neutral0,
                        maxLines = 1
                    )
                }
            }
            Row {
                Text(
                    text = "진행 날짜: ",
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral600
                )
                Text(
                    text = dateText,
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral600,
                    maxLines = 1
                )
            }
            Row {
                Text(
                    text = "행사 소개: ",
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral600
                )
                Text(
                    text = eventIntroText,
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailEventBoxPreview() {
    DetailEventBox(
        eventName = "2025년 제22회 깔끔한 행사 명 선발 대회",
        stateText = "곧 행사 진행",
        dateText = "2025.07.20 09:00 ~ 2025.07.21 09:00",
        eventIntroText = "2025년 제22회 깔끔한 행사 명 선발 대회 소개입니다.",
        stateColor = KoinTheme.colors.primary400
    )
}