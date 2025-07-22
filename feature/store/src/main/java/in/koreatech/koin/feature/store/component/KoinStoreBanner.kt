package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.EVENT_BANNER_AUTO_SCROLL_MILLISECONDS
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.LocalStoreBanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KoinStoreBanner(
    bannerList: List<LocalStoreBanner>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % bannerList.size
    ) { Int.MAX_VALUE }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        launch {
            delay(EVENT_BANNER_AUTO_SCROLL_MILLISECONDS)
            coroutineScope.launch {
                if (!pagerState.isScrollInProgress) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageSpacing = 6.dp,
            contentPadding = PaddingValues(horizontal = 24.dp),
            beyondViewportPageCount = 1
        ) { page ->
            val realPage = page % bannerList.size

            KoinStoreBannerItem(
                name = bannerList[realPage].name,
                imageUrl = bannerList[realPage].imageUrl,
                bannerCategory = bannerList[realPage].category,
                modifier = Modifier.fillMaxWidth(),
                currentIndex = realPage + 1,
                totalIndex = bannerList.size
            ) {
                onClick(bannerList[page].id)
            }
        }
    }
}

@Composable
private fun KoinStoreBannerItem(
    name: String,
    imageUrl: String,
    currentIndex: Int,
    totalIndex: Int,
    bannerCategory: BannerCategory,
    modifier: Modifier = Modifier,
    colors: KoinStoreBannerItemColor = koinStoreBannerItemColors(bannerCategory),
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .clip(RebrandKoinTheme.shapes.large)
            .background(colors.backgroundColor, RebrandKoinTheme.shapes.large)
            .clickable { onClick() },
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.chipBackgroundColor, CircleShape)
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                ) {
                    BasicText(
                        text = stringResource(R.string.store_banner_chip_text),
                        style = RebrandKoinTheme.typography.bold12.copy(
                            color = colors.chipTextColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                BasicText(
                    text = buildAnnotatedString {
                        withStyle(
                            RebrandKoinTheme.typography.bold20.toSpanStyle().copy(
                                fontWeight = FontWeight.W700,
                                color = colors.storeNameColor
                            )
                        ) {
                            append(name)
                        }
                        withStyle(
                            RebrandKoinTheme.typography.medium12.toSpanStyle().copy(
                                color = colors.storeNameColor
                            )
                        ) {
                            append(stringResource(R.string.store_banner_title))
                        }
                    }
                )

                BasicText(
                    text = stringResource(R.string.store_banner_subtitle),
                    style = RebrandKoinTheme.typography.bold14.copy(
                        color = colors.textColor
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SubcomposeAsyncImage(
                modifier = Modifier.height(80.dp),
                model = imageUrl,
                contentDescription = null,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colors.textColor
                        )
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 20.dp)
                .clip(KoinTheme.shapes.medium)
                .background(RebrandKoinTheme.colors.neutral600.copy(alpha = 0.6f), KoinTheme.shapes.medium)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_store_banner_left),
                contentDescription = null
            )

            Text(
                text = "$currentIndex/$totalIndex",
                style = KoinTheme.typography.regular10.copy(
                    color = RebrandKoinTheme.colors.neutral0
                )
            )

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_store_banner_right),
                contentDescription = null
            )
        }
    }
}

enum class BannerCategory {
    CHICKEN,
    BBQ,
    PIZZA,
    KOREAN,
    LUNCHBOX,
    PUB,
    JOKBAL,
    CAFE,
    CHINESE,
    VAN
}

@Composable
@Stable
fun koinStoreBannerItemColors(
    bannerCategory: BannerCategory
): KoinStoreBannerItemColor {
    return when (bannerCategory) {
        BannerCategory.CHICKEN -> KoinStoreBannerItemColor(
            backgroundColor = KoinTheme.colors.primary200,
            textColor = Color(0xFF1767C2),
            storeNameColor = RebrandKoinTheme.colors.neutral0,
            chipBackgroundColor = Color(0xFF1767C2),
            chipTextColor = RebrandKoinTheme.colors.warning500
        )

        BannerCategory.BBQ -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFF6B8E23),
            textColor = Color(0xFFFFF3B0),
            storeNameColor = RebrandKoinTheme.colors.neutral700,
            chipBackgroundColor = Color(0xFFFFF3B0),
            chipTextColor = RebrandKoinTheme.colors.neutral800
        )

        BannerCategory.PIZZA -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFFF64538),
            textColor = Color(0xFF7BE0AD),
            storeNameColor = RebrandKoinTheme.colors.neutral0,
            chipBackgroundColor = Color(0xFF7BE0AD),
            chipTextColor = Color(0xFFF1FFF8)
        )

        BannerCategory.KOREAN -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFF7B2D26),
            textColor = Color(0xFFFFD6BA),
            storeNameColor = Color(0xFFF5E6DE),
            chipBackgroundColor = Color(0xFFFFD6BA),
            chipTextColor = RebrandKoinTheme.colors.neutral800
        )

        BannerCategory.LUNCHBOX -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFFB5E61D),
            textColor = Color(0xFF2F2F2F),
            storeNameColor = RebrandKoinTheme.colors.neutral0,
            chipBackgroundColor = Color(0xFF2F2F2F),
            chipTextColor = RebrandKoinTheme.colors.warning500
        )

        BannerCategory.PUB -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFF2C1E14),
            textColor = Color(0xFFF4A261),
            storeNameColor = Color(0xFFF5E6DE),
            chipBackgroundColor = Color(0xFFF4A261),
            chipTextColor = RebrandKoinTheme.colors.neutral800
        )

        BannerCategory.JOKBAL -> KoinStoreBannerItemColor(
            backgroundColor = RebrandKoinTheme.colors.warning500,
            textColor = Color(0xFFFB714C),
            storeNameColor = RebrandKoinTheme.colors.neutral0,
            chipBackgroundColor = Color(0xFFFB714C),
            chipTextColor = RebrandKoinTheme.colors.warning500
        )

        BannerCategory.CAFE -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFFFFC9B9),
            textColor = Color(0xFF8FB996),
            storeNameColor = RebrandKoinTheme.colors.neutral0,
            chipBackgroundColor = Color(0xFF8FB996),
            chipTextColor = Color(0xFF3A3A3A)
        )

        BannerCategory.CHINESE -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFFF0E5D8),
            textColor = Color(0xFF2A6F4E),
            storeNameColor = RebrandKoinTheme.colors.neutral700,
            chipBackgroundColor = Color(0xFF2A6F4E),
            chipTextColor = RebrandKoinTheme.colors.warning500
        )

        BannerCategory.VAN -> KoinStoreBannerItemColor(
            backgroundColor = Color(0xFFF3F4F6),
            textColor = Color(0xFF219EBC),
            storeNameColor = Color(0xFF73737C),
            chipBackgroundColor = Color(0xFF219EBC),
            chipTextColor = Color(0xFF3A3A3A)
        )
    }
}

@Immutable
class KoinStoreBannerItemColor(
    val backgroundColor: Color,
    val textColor: Color,
    val storeNameColor: Color,
    val chipBackgroundColor: Color,
    val chipTextColor: Color
)

@Preview(showBackground = true)
@Composable
private fun KoinStoreBannerPreview() {
    KoinTheme {
        KoinStoreBanner(
            bannerList = listOf(
                LocalStoreBanner(
                    id = 1,
                    name = "치킨",
                    imageUrl = "https://example.com/chicken.jpg",
                    category = BannerCategory.CHICKEN
                ),
                LocalStoreBanner(
                    id = 2,
                    name = "피자",
                    imageUrl = "https://example.com/pizza.jpg",
                    category = BannerCategory.PIZZA
                ),
                LocalStoreBanner(
                    id = 3,
                    name = "중식",
                    imageUrl = "https://example.com/chinese.jpg",
                    category = BannerCategory.CHINESE
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun KoinStoreBannerItemPreview() {
    KoinTheme {
        KoinStoreBannerItem(
            name = "치킨",
            imageUrl = "https://example.com/chicken.jpg",
            modifier = Modifier.fillMaxWidth(),
            currentIndex = 1,
            totalIndex = 3,
            bannerCategory = BannerCategory.CHICKEN
        )
    }
}
