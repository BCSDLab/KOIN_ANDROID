package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.feature.home.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun DiningPager(
    data: ImmutableList<DiningPagerData>,
    modifier: Modifier = Modifier,
    onCardClick: (data: DiningPagerData) -> Unit = { }
) {
    val pagerState = rememberPagerState(pageCount = { data.size })
    val coroutineScope = rememberCoroutineScope()

    if (pagerState.pageCount == 0) {
        DiningEmpty(
            modifier = Modifier.fillMaxWidth()
        )
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            DiningPagerContent(
                data = data[page],
                onClick = onCardClick
            )
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
        ) {
            repeat(pagerState.pageCount) { page ->
                val isSelected = pagerState.currentPage == page

                DiningPagerIndicator(
                    place = data[page].place,
                    isSelected = isSelected
                ) {
                    if (isSelected) return@DiningPagerIndicator
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.MENU_CORNER, data[page].place)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                }
            }
        }
    }
}

@Composable
private fun DiningEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(0.5.dp, Color(0xFFE6E6E6), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(0xFFFFFFFF))
            .padding(vertical = 20.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.dining_empty),
            style = RebrandKoinTheme.typography.medium13,
            color = Color(0xFFA8A8A8),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DiningPagerContent(
    data: DiningPagerData,
    modifier: Modifier = Modifier,
    onClick: (data: DiningPagerData) -> Unit = { }
) {
    val click = remember(data) { { onClick(data) } }

    Column(
        modifier = modifier
            .border(0.5.dp, Color(0xFFE6E6E6), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(0xFFFFFFFF))
            .clickable(onClick = click)
            .padding(vertical = 20.dp, horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dining_open_time, data.startTime, data.endTime),
                style = RebrandKoinTheme.typography.regular12,
                color = Color(0xFF6F6F6F)
            )

            val price = stringResource(R.string.dining_price, data.price)
            val kcal = stringResource(R.string.dining_kcal, data.kcal)

            Text(
                text = buildAnnotatedString {
                    withStyle(RebrandKoinTheme.typography.regular12.toSpanStyle().copy(color = Color(0xFF6F6F6F))) {
                        append(price)
                        append(" · ")
                    }

                    withStyle(RebrandKoinTheme.typography.medium12.toSpanStyle().copy(color = Color(0xFF4B4B4B))) {
                        append(kcal)
                    }
                }
            )
        }

        Text(
            text = data.place,
            style = RebrandKoinTheme.typography.bold20,
            color = Color(0xFF323232)
        )

        Text(
            text = data.menu.joinToString(" · "),
            style = RebrandKoinTheme.typography.regular14,
            color = Color(0xFF6F6F6F),
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DiningPagerIndicator(
    place: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(0.5.dp, color = if (isSelected) Color(0xFFB611F5) else Color(0xFFE6E6E6), CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(if (isSelected) Color(0xFFB611F5) else Color(0xFFFFFFFF))
            .padding(vertical = 8.dp, horizontal = 14.dp)
    ) {
        Text(
            text = place,
            style = RebrandKoinTheme.typography.medium13,
            color = if (isSelected) Color(0xFFFFFFFF) else Color(0xFF6F6F6F)
        )
    }
}

@Preview
@Composable
private fun DiningPagerContentPreview() {
    DiningPagerContent(
        data = DiningPagerData(
            place = DiningPlace.CornerA.place,
            startTime = "07:30",
            endTime = "09:30",
            price = 5000,
            kcal = 620,
            menu = persistentListOf("계란말이", "북엇국", "시금치나물", "깍두기")
        )
    )
}

@Preview
@Composable
private fun DiningPagerIndicatorPreview() {
    DiningPagerIndicator(
        "A코너",
        isSelected = false
    )
}

@Preview
@Composable
private fun DiningPagerPreview() {
    DiningPager(
        data = persistentListOf(
            DiningPagerData(
                place = DiningPlace.CornerA.place,
                startTime = "07:30",
                endTime = "09:30",
                price = 5000,
                kcal = 620,
                menu = persistentListOf("계란말이", "북엇국", "시금치나물", "깍두기")
            ),
            DiningPagerData(
                place = DiningPlace.CornerB.place,
                startTime = "07:30",
                endTime = "09:30",
                price = 5000,
                kcal = 945,
                menu = persistentListOf("등심돈까스", "흰밥", "미트소스파스타", "깍두기", "생야채샐러드")
            )
        )
    )
}

data class DiningPagerData(
    val place: String,
    val startTime: String,
    val endTime: String,
    val price: Int,
    val kcal: Int,
    val menu: ImmutableList<String>
)
