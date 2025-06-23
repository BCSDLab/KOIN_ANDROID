package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.Store
import java.time.LocalTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoreCard(
    store: Store,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (statusText, showOverlay) = getStoreStatus(store.open)

    Box {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .border(
                    width = 0.5.dp,
                    color = RebrandKoinTheme.colors.neutral200,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = RebrandKoinTheme.colors.neutral0
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(0.5.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
//                painter = rememberAsyncImagePainter(imageUrl),
                    painter = painterResource(id = R.drawable.testchicken),
                    contentDescription = "${store.name} 대표 이미지",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .width((0.35f * LocalConfiguration.current.screenWidthDp).dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 0.5.dp,
                            color = RebrandKoinTheme.colors.neutral400,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 15.dp, end = 56.dp, bottom = 15.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = store.name,
                        style = RebrandKoinTheme.typography.bold16.copy(
                            fontSize = 16.sp,
                            color = KoinTheme.colors.neutral800
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "평점",
                            tint = if (store.reviewCount == 0) RebrandKoinTheme.colors.neutral300 else RebrandKoinTheme.colors.warning500
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", store.averageRate),
                            style = RebrandKoinTheme.typography.bold12,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when {
                                store.reviewCount == 0 -> "리뷰 없음"
                                store.reviewCount > 10 -> "( 리뷰 10개 이상 )"
                                else -> "( 리뷰 ${store.reviewCount}개 )"
                            },
                            style = RebrandKoinTheme.typography.regular12,
                            color = Color.Gray
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_motorcycle),
                            contentDescription = "배달",
                            tint = RebrandKoinTheme.colors.primary400
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "배달비 2,000원",
                            style = RebrandKoinTheme.typography.regular12
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        store.benefitDetails.forEach { benefit ->
                            Badge(text = benefit)
                        }
                    }
                }
            }
        }
        if (showOverlay) {
            Box(
                modifier = modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.6f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText,
                    color = Color.White,
                    style = RebrandKoinTheme.typography.bold16
                )
            }
        }
    }
}

@Composable
fun Badge(text: String) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF1F3F5),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.regular10,
            color = RebrandKoinTheme.colors.neutral600
        )
    }
}

private fun getStoreStatus(openData: Store.OpenData): Pair<String, Boolean> {
    val currentTime = LocalTime.now()

    if (openData.closed) return "영업이 종료된 가게에요!" to true

    if (openData.openTime.isNotEmpty() && openData.closeTime.isNotEmpty()) {
        val openTime = LocalTime.parse(openData.openTime)
        val closeTime = LocalTime.parse(openData.closeTime)
        if (currentTime.isBefore(openTime)) {
            val ampm = if (openTime.hour < 12) "오전" else "오후"
            val hour = openTime.hour
            val minute = openTime.minute.toString().padStart(2, '0')
            return "${ampm} ${hour}:${minute}시 오픈" to true
        } else if (currentTime.isAfter(closeTime)) {
            return "영업이 종료된 가게에요!" to true
        }
    }
    if (!openData.openStore()) return "영업을 준비중이에요" to true

    return "" to false
}

@Preview(showBackground = true)
@Composable
fun StoreCardPreview() {
    val dummyStore = Store(
        uid = 1,
        name = "치킨집",
        phone = "010-1234-5678",
        isDeliveryOk = true,
        isCardOk = true,
        isBankOk = false,
        isEvent = true,
        isOpen = true,
        averageRate = 4.7,
        reviewCount = 15,
        open = Store.OpenData(
            dayOfWeek = "월요일",
            closed = false,
            openTime = "10:00",
            closeTime = "22:00"
        ),
        categoryIds = listOf(1, 2),
        benefitDetails = listOf("포장 할인", "쿠폰 사용 가능", "test", "test2", "test3")
    )
    val dummyImageUrl = ""

    StoreCard(
        store = dummyStore,
        imageUrl = dummyImageUrl,
        onClick = {}
    )
}
