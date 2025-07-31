package `in`.koreatech.koin.feature.store.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.MenuCategoryChips
import `in`.koreatech.koin.feature.store.component.OrderBottomBar
import `in`.koreatech.koin.feature.store.component.menuListSection
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.collapseToolbar
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import `in`.koreatech.koin.feature.store.util.customCollapsingToolbarContent
import `in`.koreatech.koin.feature.store.viewmodel.ShoppingCartViewModel
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    modifier: Modifier = Modifier,
    isCartModified: Boolean = false,
    viewModel: StoreDetailViewModel = hiltViewModel(),
    cartViewModel: ShoppingCartViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: () -> Unit = {},
    navigateToReview: () -> Unit = {},
    navigateToMenuInfo: (menuId: Int) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val cartUiState by cartViewModel.collectAsState()
    val pagerState = rememberPagerState(0, 0f) {
        uiState.store.imageUrls?.size ?: 0
    }

    val rememberState = rememberCollapsingToolbarState(
        toolbarMinHeight = 64.dp
    )
    val overlayAlpha = rememberState.progress()
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isCartModified) {
        snapshotFlow { isCartModified }
            .distinctUntilChanged()
            .onEach {
                if (it) {
                    cartViewModel.getCart(cartUiState.cartType)
                }
            }
            .launchIn(coroutineScope)
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            uiState.isLogin
        }.filter { it }.onEach {
            viewModel.getCartItemsCount()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f),
            contentAlignment = Alignment.Center
        ) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(color = colorResource(id = R.color.store_detail_background))
                .nestedScroll(nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = rememberState.toolbarMinHeight)
                    .offset {
                        IntOffset(
                            0,
                            currentToolbarHeightDp.value.toPx().roundToInt() + rememberState.toolbarMinHeight.toPx().roundToInt()
                        )
                    },
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
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = KoinTheme.colors.neutral100,
                            thickness = 8.dp
                        )
                    }
                }
                stickyHeader {
                    MenuCategoryChips(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 66.dp),
                        menuCategories = uiState.categories,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        if (uiState.isOrderableShop) {
                            navigateToMenuInfo(it)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            KoinStoreTopAppBar(
                title = uiState.store.name,
                onNavigationIconClick = {
                    navigateToBack()
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = {
                            navigateToCart()
                        }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            navigateToCart()
                        }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null
                            )
                        }
                        Box(
                            modifier = Modifier
                                .offset(x = (-5).dp, y = 5.dp)
                                .size(16.dp)
                                .background(RebrandKoinTheme.colors.primary500, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${uiState.cartItemCount}",
                                style = RebrandKoinTheme.typography.medium12.copy(
                                    color = RebrandKoinTheme.colors.neutral0,
                                    lineHeightStyle = LineHeightStyle(
                                        trim = LineHeightStyle.Trim.Both,
                                        alignment = LineHeightStyle.Alignment.Center
                                    )
                                )
                            )
                        }
                    }
                },
                overlayAlpha = overlayAlpha
            ) {
                StoreDetailImage(
                    modifier = Modifier
                        .heightIn(rememberState.toolbarMinHeight, rememberState.toolbarMaxHeight + statusBarHeight)
                        .fillMaxWidth()
                        .customCollapsingToolbarContent(
                            maxToolbarHeight = rememberState.toolbarMaxHeight,
                            currentToolbarHeight = currentToolbarHeightDp.value,
                            overlayAlpha = overlayAlpha.value
                        ),
                    imageUrls = uiState.store.imageUrls ?: emptyList(),
                    pagerState = pagerState
                )
            }
        }
        if (cartUiState.cart.items.isNotEmpty()) {
            OrderBottomBar(
                itemCount = cartUiState.cart.items.count(),
                totalPrice = cartUiState.cart.totalAmount,
                isOrderEnabled = cartUiState.cartValidation == CartValidation.VALID,
                orderableMessage = cartUiState.isValidateCart.message,
                navigateToCart = navigateToCart
            )
        }
    }
}
