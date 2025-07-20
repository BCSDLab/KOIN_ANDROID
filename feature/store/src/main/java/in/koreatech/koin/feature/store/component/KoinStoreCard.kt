package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.FilterBadge

@Composable
fun KoinStoreCard(
    storeName: String,
    storeAverageRating: String,
    storeReviewCount: Int,
    storeDeliveryFee: String,
    storeImageUrl: String,
    modifier: Modifier = Modifier,
    isOpen: Boolean = true,
    filterBadgeList: List<FilterBadge> = emptyList(),
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .clip(RebrandKoinTheme.shapes.small)
            .background(RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.small)
            .clickable { onClick() }
    ) {
        if (!isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.6f))
                    .zIndex(2f),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = stringResource(R.string.store_closed),
                    style = RebrandKoinTheme.typography.bold16.copy(color = RebrandKoinTheme.colors.neutral0)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SubcomposeAsyncImage(
                model =
                ImageRequest.Builder(LocalContext.current)
                    .data(storeImageUrl)
                    .crossfade(true)
                    .build(),
                loading = {
                    CircularProgressIndicator()
                },
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(RebrandKoinTheme.shapes.small)
            )

            Column {
                BasicText(
                    text = storeName,
                    style = RebrandKoinTheme.typography.bold16
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .paint(
                                painter = painterResource(R.drawable.ic_rating)
                            )
                    )

                    BasicText(
                        text = storeAverageRating,
                        style = RebrandKoinTheme.typography.bold12
                    )

                    BasicText(
                        text = stringResource(R.string.store_review_count, storeReviewCount),
                        style = RebrandKoinTheme.typography.regular12.copy(
                            color = Color(0xFF767676)
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .paint(
                                painter = painterResource(R.drawable.ic_delivery)
                            )
                    )

                    BasicText(
                        text = stringResource(R.string.store_delivery_fee, storeDeliveryFee),
                        style = RebrandKoinTheme.typography.regular12.copy(
                            color = RebrandKoinTheme.colors.neutral600
                        )
                    )
                }

                if (filterBadgeList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filterBadgeList.forEach { badge ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF2F2F2), shape = CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                BasicText(
                                    text = stringResource(badge.stringResId),
                                    style = RebrandKoinTheme.typography.regular12.copy(
                                        color = RebrandKoinTheme.colors.neutral600,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        lineHeightStyle = LineHeightStyle(
                                            trim = LineHeightStyle.Trim.Both,
                                            alignment = LineHeightStyle.Alignment.Center
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinStoreCardPreview() {
    RebrandKoinTheme {
        KoinStoreCard(
            modifier = Modifier.fillMaxWidth(),
            storeImageUrl = "https://example.com/store_image.jpg",
            storeName = "Koin Store",
            storeAverageRating = "4.5",
            storeReviewCount = 5,
            storeDeliveryFee = "2,500",
            isOpen = true,
            filterBadgeList = listOf(
                FilterBadge.SERVICE,
                FilterBadge.DELIVERY_AVAILABLE
            )
        )
    }
}
