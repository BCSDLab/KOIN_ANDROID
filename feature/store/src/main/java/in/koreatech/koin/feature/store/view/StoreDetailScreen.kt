package `in`.koreatech.koin.feature.store.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import `in`.koreatech.feature.store.util.CustomClosingToolbarScreenDefaults
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreReviewStatistics
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.MenuCategoryChips
import `in`.koreatech.koin.feature.store.component.MenuListSection
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.CustomCollapsingToolbarState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun StoreDetailScreen(
    storeInfo: StoreWithMenu,
    categories: List<MenuCategory>,
    menus: List<StoreMenuCategories>,
    pagerState: PagerState
) {
    val toolbarState = remember { CustomCollapsingToolbarState() }
    val rememberState = toolbarState.rememberCollapsingToolbarState(
        toolbarMinHeight = 40.dp,
        toolbarMaxHeight = 300.dp
    )
    val progress = toolbarState.progress(rememberState)
    val overlayAlpha = (progress).coerceIn(0f, 1f)
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = toolbarState.toolbarHeightPx,
        minHeightPx = toolbarState.minHeightPx
    )
    val currentToolbarHeightDp = toolbarState.currentToolbarHeightDp(rememberState)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.store_detail_background))
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = currentToolbarHeightDp + CustomClosingToolbarScreenDefaults.windowInsets
                        .asPaddingValues()
                        .calculateTopPadding()
                ),
            state = rememberState.listState
        ) {
            item {
                Column {
                    StoreDetailInfo(
                        storeInfo = storeInfo,
                        storeReview = StoreReview(
                            totalCount = 0,
                            currentCount = 0,
                            totalPage = 0,
                            currentPage = 0,
                            statistics = StoreReviewStatistics(
                                averageRating = 0.0,
                                ratings = emptyMap()
                            ),
                            reviews = emptyList()
                        )
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = KoinTheme.colors.neutral100,
                        thickness = 8.dp
                    )
                }
            }
            stickyHeader {
                MenuCategoryChips(categories)
            }
            item {
                repeat(categories.size) { index ->
                    val menuList = menus.getOrNull(index)?.menus
                    if (!menuList.isNullOrEmpty()) {
                        MenuListSection(
                            category = categories[index].categoryName,
                            menus = menuList
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.statusBarsPadding()
                .fillMaxWidth()
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = lerp(KoinTheme.colors.neutral800, KoinTheme.colors.neutral0, 1f - overlayAlpha)
                )
            }
            Text(
                text = storeInfo.name,
                fontWeight = Bold,
                color = KoinTheme.colors.neutral800.copy(alpha = overlayAlpha)
            )
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                        contentDescription = null,
                        tint = lerp(KoinTheme.colors.neutral800, KoinTheme.colors.neutral0, 1f - overlayAlpha)
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(x = (-5).dp, y = 5.dp)
                        .size(16.dp)
                        .background(Color.Magenta, CircleShape)
                ) {
                    Text(
                        text = "3",
                        fontSize = 10.sp,
                        lineHeight = 11.sp,
                        color = KoinTheme.colors.neutral0,
                        modifier = Modifier.padding(start = 4.dp, bottom = 0.5.dp)
                    )
                }
            }
        }

        StoreDetailImage(
            modifier = Modifier
                .height(toolbarState.toolbarMaxHeight)
                .offset { IntOffset(0, rememberState.toolbarOffsetPx.floatValue.roundToInt()) }
                .fillMaxWidth(),
            imageUrls = storeInfo.imageUrls ?: emptyList(),
            alpha = overlayAlpha,
            pagerState = pagerState
        )
    }
}
