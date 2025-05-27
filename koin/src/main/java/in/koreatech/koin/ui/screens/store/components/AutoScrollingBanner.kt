package `in`.koreatech.koin.ui.screens.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.StoreEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoScrollingBanner(
    storeEvents: List<StoreEvent>,
    modifier: Modifier = Modifier,
    autoScrollMillis: Long = 3000,
    resumeDelayMillis: Long = 3000,
    onItemClick: (StoreEvent) -> Unit = {}
) {
    val realItemCount = storeEvents.size

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val pageWidthRatio = 0.9f
    val pageWidth = (screenWidth * pageWidthRatio).dp

    val sidePeek = (((1f - pageWidthRatio) / 2f) * screenWidth).dp

    val loopedBannerList = remember(storeEvents) {
        if (storeEvents.size > 1) {
            listOf(storeEvents.last()) + storeEvents + listOf(storeEvents.first())
        } else {
            storeEvents
        }
    }

    val pagerState = rememberPagerState(
        initialPage = if (storeEvents.size > 1) 1 else 0,
        pageCount = { loopedBannerList.size }
    )

    var autoScrollEnabled by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.settledPage, autoScrollEnabled) {
        if (realItemCount > 1 && autoScrollEnabled) {
            delay(autoScrollMillis)
            if (!pagerState.isScrollInProgress) {
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    LaunchedEffect(lastInteractionTime) {
        if (!autoScrollEnabled) {
            delay(resumeDelayMillis)
            autoScrollEnabled = true
        }
    }

    LaunchedEffect(pagerState.currentPage, realItemCount) {
        if (realItemCount > 1) {
            when (pagerState.currentPage) {
                0 -> pagerState.scrollToPage(realItemCount)
                realItemCount + 1 -> pagerState.scrollToPage(1)
            }
        }
    }

    val bannerHeight = 100.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(bannerHeight)
            .wrapContentSize(Alignment.Center)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = sidePeek),
            pageSpacing = 6.dp,
            modifier = Modifier
                .width(screenWidth.dp)
                .align(Alignment.Center)
                .height(bannerHeight)
        ) { page ->
            val event = loopedBannerList[page]
            Box(
                Modifier
                    .width(pageWidth)
                    .height(bannerHeight)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onItemClick(event) }
            ) {
                AsyncImage(
                    model = event.thumbnailImages?.firstOrNull(),
                    contentDescription = event.shopName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_event_image),
                    error = painterResource(R.drawable.default_event_image)
                )
                Text(
                    text = event.shopName,
                    color = KoinTheme.colors.neutral0,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(8.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .zIndex(2f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "이전 배너",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(KoinTheme.colors.neutral0.copy(alpha = 0.7f))
                    .clickable(enabled = realItemCount > 1) {
                        autoScrollEnabled = false
                        lastInteractionTime = System.currentTimeMillis()
                        coroutineScope.launch {
                            val prevPage =
                                if (pagerState.currentPage == 0) {
                                    realItemCount
                                } else {
                                    pagerState.currentPage - 1
                                }
                            pagerState.animateScrollToPage(prevPage)
                        }
                    }
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "다음 배너",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(KoinTheme.colors.neutral0.copy(alpha = 0.7f))
                    .clickable(enabled = realItemCount > 1) {
                        autoScrollEnabled = false
                        lastInteractionTime = System.currentTimeMillis()
                        coroutineScope.launch {
                            val nextPage =
                                if (pagerState.currentPage == realItemCount + 1) {
                                    1
                                } else {
                                    pagerState.currentPage + 1
                                }
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                    .padding(4.dp)
            )
        }
    }
}
