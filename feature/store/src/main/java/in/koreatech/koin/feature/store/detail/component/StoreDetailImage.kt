package `in`.koreatech.koin.feature.store.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.HorizontalPagerIndicator
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.collections.immutable.ImmutableList

@SuppressLint("SuspiciousIndentation")
@Composable
fun StoreDetailImage(
    modifier: Modifier,
    imageUrls: ImmutableList<String>,
    pagerState: PagerState
) {
    Box(
        modifier = modifier
            .background(KoinTheme.colors.neutral500),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (imageUrls.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls[page])
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = imageUrls.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            activeColor = Color.White,
            inactiveColor = Color.LightGray
        )
    }
}
