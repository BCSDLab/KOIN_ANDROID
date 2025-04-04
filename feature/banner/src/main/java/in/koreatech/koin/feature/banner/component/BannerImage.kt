package `in`.koreatech.koin.feature.banner.component

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.constant.KOIN_PLAYSTORE_URL
import `in`.koreatech.koin.feature.banner.BANNER_AUTO_SCROLL_MILLISECONDS
import `in`.koreatech.koin.feature.banner.R
import `in`.koreatech.koin.feature.banner.model.LocalBanner
import `in`.koreatech.koin.feature.banner.util.ImageUtil
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@Composable
fun BannerImage(
    bannerList: ImmutableList<LocalBanner>,
    currentKoinVersion: Int,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {}
) {
    val pagerState = rememberPagerState { Int.MAX_VALUE }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.isScrollInProgress }
            .filter { it == false }
            .collectLatest {
                if (bannerList.size > 1) {
                    delay(BANNER_AUTO_SCROLL_MILLISECONDS)
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
    }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState
        ) { page ->
            val realPage = page % bannerList.size
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
                        append("${(pagerState.currentPage % bannerList.size) + 1}")
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
    SubcomposeAsyncImage(
        modifier = modifier.fillMaxSize().noRippleClickable {
            if (banner.redirectLink == null) return@noRippleClickable
            if (banner.version > currentKoinVersion) { // If the banner link requires a higher version of the app
                ToastUtil.getInstance().makeShort(R.string.banner_require_new_version)
                val intent = Intent(Intent.ACTION_VIEW, KOIN_PLAYSTORE_URL.toUri())
                context.startActivity(intent)
            } else {
                if (banner.redirectLink.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, banner.redirectLink.toUri())
                    context.startActivity(intent)
                }
            }
            dismiss()
        },
        model = ImageRequest.Builder(LocalContext.current)
            .data(ImageUtil.getResizedImageUrl(banner.imageUrl, width = 400))
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
