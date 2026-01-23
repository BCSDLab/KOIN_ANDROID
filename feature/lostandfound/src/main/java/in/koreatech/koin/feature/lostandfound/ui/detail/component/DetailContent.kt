package `in`.koreatech.koin.feature.lostandfound.ui.detail.component

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun DetailContent(
    imageUris: List<Uri>?,
    content: String,
    isWriterAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Column(
        modifier = modifier
            .padding(vertical = 24.dp, horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        if (imageUris != null) {
            val pagerState = rememberPagerState(pageCount = {
                imageUris.size
            })
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState
            ) { page ->
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUris[page])
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                imageUris.indices.forEach { index ->
                    Image(
                        modifier = if (index == pagerState.currentPage) {
                            Modifier.size(6.dp)
                        } else {
                            Modifier.size(
                                4.dp
                            )
                        },
                        painter = if (index == pagerState.currentPage) {
                            painterResource(R.drawable.ic_image_page_indicator_filled)
                        } else {
                            painterResource(id = R.drawable.ic_image_page_indicator_not_filled)
                        },
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = content,
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral800
        )

        if (isWriterAdmin) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .clip(KoinTheme.shapes.medium)
                    .fillMaxWidth()
                    .background(KoinTheme.colors.neutral100)
                    .padding(vertical = 16.dp, horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                val infoMessage = buildAnnotatedString {
                    withStyle(KoinTheme.typography.regular12.toSpanStyle()) {
                        append(stringResource(R.string.detail_student_association_info_1))
                    }
                    withStyle(
                        KoinTheme.typography.regular12.copy(fontWeight = FontWeight.Bold)
                            .toSpanStyle()
                    ) {
                        append(stringResource(R.string.detail_student_association_info_2))
                    }
                    withStyle(KoinTheme.typography.regular12.toSpanStyle()) {
                        append(stringResource(R.string.detail_student_association_info_3))
                    }
                }
                Text(
                    text = infoMessage,
                    textAlign = TextAlign.Center,
                    style = KoinTheme.typography.regular12
                )
            }
        }
    }
}
