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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
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
import `in`.koreatech.koin.feature.store.component.MenuListSection
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.CustomCollapsingToolbarState
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun StoreDetailScreen(
    viewModel: StoreDetailViewModel = hiltViewModel(),
    pagerState: PagerState,
    navigateToCart: () -> Unit ={},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: () -> Unit = {},
    navigateToReview: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

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
                MenuCategoryChips(uiState.categories.menuCategories ?: emptyList(),
                    onCategoryClicked = { categoryId ->
                        viewModel.clickMenuCategory(categoryId)
                        val targetIndex = uiState.categories.menuCategories
                            ?.indexOfFirst { it.id == categoryId } ?: 0
                        coroutineScope.launch {
                            rememberState.listState.animateScrollToItem(targetIndex + 2, -170)
                        }

                        toolbarState.collapseToolbar(
                            state = rememberState
                        )
                    }
                )
            }
            repeat(uiState.categories.menuCategories?.size ?: 0) { index ->
                item {
                    val menuList = uiState.categories.menuCategories?.getOrNull(index)?.menus
                    if (!menuList.isNullOrEmpty()) {
                        uiState.categories.menuCategories?.getOrNull(index).let {
                            MenuListSection(
                                category = it?.name ?: "",
                                menus = menuList
                            )
                        }
                    }
                }
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
                .height(toolbarState.toolbarMaxHeight)
                .offset { IntOffset(0, rememberState.toolbarOffsetPx.floatValue.roundToInt()) }
                .fillMaxWidth(),
            imageUrls = uiState.store.imageUrls ?: emptyList(),
            alpha = overlayAlpha,
            pagerState = pagerState
        )
    }
}
