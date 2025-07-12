package `in`.koreatech.koin.feature.store.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.feature.store.util.CustomClosingToolbarScreenDefaults
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.MenuCategoryChips
import `in`.koreatech.koin.feature.store.component.menuListSection
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.collapseToolbar
import `in`.koreatech.koin.feature.store.state.currentToolbarHeightDp
import `in`.koreatech.koin.feature.store.state.progress
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoreDetailScreen(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    pagerState: PagerState,
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: () -> Unit = {},
    navigateToReview: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    val rememberState = rememberCollapsingToolbarState()
    val progress = rememberState.progress()
    val overlayAlpha = (progress).coerceIn(0f, 1f)
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()
    val coroutineScope = rememberCoroutineScope()
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
                        storeInfo = uiState.store,
                        storeReview = uiState.storeReview,
                        storeDescriptionModel = uiState.shopDescription,
                        navigateToReview = { navigateToReview() },
                        navigateToDetailInfo = { navigateToDetailInfo() }
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = KoinTheme.colors.neutral100,
                        thickness = 8.dp
                    )
                }
            }
            stickyHeader {
                MenuCategoryChips(
                    uiState.categories,
                    onCategoryClicked = { categoryId, stickyHeaderHeight ->
                        viewModel.clickMenuCategory(categoryId)
                        rememberState.collapseToolbar(
                            state = rememberState
                        )
                        CoroutineScope(coroutineScope.coroutineContext).launch {
                            rememberState.listState.scrollToItem(uiState.categories.indexOfFirst { it.menuGroupId == categoryId } + 2, -stickyHeaderHeight)
                        }
                    }
                )
            }
            uiState.categories.forEach { category ->
                menuListSection(
                    category = category.menuGroupName,
                    menus = category.menus,
                )
            }
        }

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navigateToBack()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = lerp(KoinTheme.colors.neutral800, KoinTheme.colors.neutral0, 1f - overlayAlpha)
                )
            }
            Text(
                text = uiState.store.name,
                fontWeight = Bold,
                color = KoinTheme.colors.neutral800.copy(alpha = overlayAlpha)
            )
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = {
                    navigateToCart()
                }) {
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
                        text = "3", // TODO: API 연동 후 장바구니 수량 표시
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
                .height(rememberState.toolbarMaxHeight)
                .offset { IntOffset(0, rememberState.toolbarOffsetPx.floatValue.roundToInt()) }
                .fillMaxWidth(),
            imageUrls = uiState.store.imageUrls ?: emptyList(),
            alpha = overlayAlpha,
            pagerState = pagerState
        )
    }
}
