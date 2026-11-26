package `in`.koreatech.koin.feature.banner.component

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.analytics.BannerAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventCategory
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import `in`.koreatech.koin.domain.constant.KOIN_PLAYSTORE_URL
import `in`.koreatech.koin.feature.banner.BANNER_AUTO_SCROLL_MILLISECONDS
import `in`.koreatech.koin.feature.banner.R
import `in`.koreatech.koin.feature.banner.model.LocalBanner
import `in`.koreatech.koin.feature.banner.util.ImageUtil
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BannerImage(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    onBannerIndexChange: (Int) -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier) {
        // A zero-sized page with zero spacing cannot advance Pager's measurement loop.
        if (constraints.maxWidth > 0 && bannerList.isNotEmpty()) {
            key(bannerList.size) {
                BannerPager(bannerList, currentKoinVersion, dismiss, onBannerIndexChange)
            }
        }
    }
}

@Composable
private fun BannerPager(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    dismiss: () -> Unit,
    onBannerIndexChange: (Int) -> Unit
) {
    val looping = bannerList.size > 1
    // Only two sentinel pages are needed for seamless wrapping in either direction.
    val pagerState = rememberPagerState(initialPage = if (looping) 1 else 0) {
        if (looping) bannerList.size + 2 else 1
    }
    fun bannerIndex(page: Int) = (page - 1).mod(bannerList.size)

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (looping && !pagerState.isScrollInProgress) {
            when (pagerState.currentPage) {
                0 -> pagerState.scrollToPage(bannerList.size)
                bannerList.size + 1 -> pagerState.scrollToPage(1)
            }
        }
    }

    LaunchedEffect(pagerState) {
        while (looping) {
            delay(BANNER_AUTO_SCROLL_MILLISECONDS)
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, bannerList.size) {
        onBannerIndexChange(bannerIndex(pagerState.currentPage))
    }

    LaunchedEffect(pagerState, bannerList) {
        var dragStartPage = -1
        launch {
            pagerState.interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is DragInteraction.Start -> dragStartPage = pagerState.settledPage
                    is DragInteraction.Cancel -> dragStartPage = -1
                }
            }
        }
        snapshotFlow { pagerState.isScrollInProgress }.collect { scrolling ->
            if (!scrolling && dragStartPage != -1) {
                val previousPage = dragStartPage
                dragStartPage = -1
                logBannerSwipe(bannerList, previousPage, pagerState.settledPage)
            }
        }
    }

    Box {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState
        ) { page ->
            val realPage = bannerIndex(page)
            BannerContent(
                banner = bannerList[realPage],
                dismiss = dismiss,
                currentKoinVersion = currentKoinVersion
            )
        }

        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .clip(KoinTheme.shapes.medium)
                    .background(Color(0x80000000))
                    .padding(horizontal = 12.dp),
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = KoinTheme.colors.neutral0)) {
                        append("${(bannerIndex(pagerState.currentPage)) + 1}")
                    }
                    withStyle(SpanStyle(color = KoinTheme.colors.neutral400)) {
                        append("/${bannerList.size}")
                    }
                },
                style = KoinTheme.typography.regular14
            )
        }
    }
}

@Composable
private fun BannerContent(
    banner: LocalBanner,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {}
) {
    val context = LocalContext.current

    val resizedImageUrl by remember(banner.imageUrl) { mutableStateOf(ImageUtil.getResizedImageUrl(banner.imageUrl, width = 400)) }

    SubcomposeAsyncImage(
        modifier = modifier
            .fillMaxSize()
            .noRippleClickable {
                EventLogger.logCustomEvent(
                    BannerAnalyticsConstant.EVENT_NAME,
                    EventCategory.CLICK.value,
                    BannerAnalyticsConstant.Label.MODAL_CLICK,
                    banner.id.toString()
                )
                if (banner.redirectLink == null) return@noRippleClickable
                if (banner.version > currentKoinVersion) { // If the banner link requires a higher version of the app
                    ToastUtil.getInstance().makeShort(R.string.banner_require_new_version)
                    val intent = Intent(Intent.ACTION_VIEW, KOIN_PLAYSTORE_URL.toUri())
                    context.startActivity(intent)
                } else {
                    if (banner.redirectLink.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, banner.redirectLink.toUri())
                        intent.`package` = context.packageName
                        context.startActivity(intent)
                    }
                }
                dismiss()
            },
        contentScale = ContentScale.Crop,
        imageLoader = KoinCoilImageLoader.getImageLoader(context),
        model = ImageRequest.Builder(LocalContext.current)
            .data(resizedImageUrl)
            .crossfade(true)
            .build(),
        alignment = Alignment.BottomCenter,
        loading = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        contentDescription = "Banner ${banner.id}"
    )
}

private fun logBannerSwipe(
    bannerList: ImmutableList<LocalBanner>,
    previousPage: Int,
    settledPage: Int
) {
    if (settledPage == previousPage) return
    val banner = bannerList[(settledPage - 1).mod(bannerList.size)]
    EventLogger.logCustomEvent(
        BannerAnalyticsConstant.EVENT_NAME,
        EventCategory.SWIPE.value,
        BannerAnalyticsConstant.Label.MODAL_NEXT_SWIPE,
        banner.id.toString()
    )
}
