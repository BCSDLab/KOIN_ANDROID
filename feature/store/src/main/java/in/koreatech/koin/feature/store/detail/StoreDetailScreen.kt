package `in`.koreatech.koin.feature.store.detail

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_DETAIL_MAIN
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.OrderBottomBar
import `in`.koreatech.koin.feature.store.detail.component.MenuCategoryChips
import `in`.koreatech.koin.feature.store.detail.component.StoreDetailImage
import `in`.koreatech.koin.feature.store.detail.component.StoreDetailInfo
import `in`.koreatech.koin.feature.store.detail.component.menuListSection
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.collapseToolbar
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import `in`.koreatech.koin.feature.store.util.customCollapsingToolbarContent
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    modifier: Modifier = Modifier,
    isCartAdded: Boolean = false,
    isCartModified: Boolean = false,
    viewModel: StoreDetailViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: (selectedInfo: String) -> Unit = {},
    navigateToReview: (StoreNavigationData) -> Unit = {},
    navigateToMenuInfo: (menuId: Int) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    viewModel.collectSideEffect {
        handleSideEffect(it, navigateToCart)
    }

    val pagerState = rememberPagerState(0, 0f) {
        uiState.store.imageUrls?.size ?: 0
    }

    val rememberState = rememberCollapsingToolbarState()
    val overlayAlpha = rememberState.progress()
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val menuCategoryHeight = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isCartModified) {
        snapshotFlow { isCartModified }
            .distinctUntilChanged()
            .onEach {
                if (it && uiState.isLoggedIn) {
                    viewModel.getCart(uiState.cartType)
                }
            }
            .launchIn(coroutineScope)
    }

    LaunchedEffect(uiState.selectedCategoryId) {
        if (currentToolbarHeightDp.value != rememberState.toolbarMinHeight) return@LaunchedEffect // Don't scroll if toolbar not collapsed
        rememberState.listState.animateScrollToItem(uiState.categories.indexOfFirst { it.menuGroupId == uiState.selectedCategoryId } + 2)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { isCartAdded }
            .distinctUntilChanged()
            .collectLatest {
                if (it) {
                    Toast.makeText(context, R.string.store_cart_add_added, Toast.LENGTH_SHORT).show()
                }
            }
    }

    LaunchedEffect(Unit, uiState.isLogin) {
        if (uiState.isLogin) {
            viewModel.getCartItemsCount()
        }
    }

    LaunchedEffect(rememberState.listState) {
        snapshotFlow { rememberState.listState.firstVisibleItemIndex }
            .collect { index ->
                val visibleCategory = uiState.categories.getOrNull(index - 2)
                visibleCategory?.let {
                    viewModel.changeCategory(it.menuGroupId)
                }
            }
    }

    if (uiState.showSignInDialog) {
        KoinStoreSignInDialog(
            onPositive = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "koin://login/login?link=$DEEPLINK_STORE_DETAIL_MAIN/${uiState.storeId}/${uiState.isOrderableShop}".toUri()
                }.apply {
                    context.startActivity(this)
                }
            },
            onNegative = viewModel::hideSignInDialog
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                },
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
                    .padding(bottom = rememberState.toolbarMinHeight + statusBarHeight)
                    .offset {
                        IntOffset(
                            0,
                            currentToolbarHeightDp.value
                                .toPx()
                                .roundToInt() + statusBarHeight
                                .toPx()
                                .roundToInt()
                        )
                    },
                state = rememberState.listState
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        StoreDetailInfo(
                            storeInfo = uiState.store,
                            storeReview = uiState.storeReview,
                            storeDescriptionModel = uiState.shopDescription,
                            navigateToReview = {
                                navigateToReview(
                                    StoreNavigationData(
                                        shopId = uiState.store.shopId,
                                        orderableShopId = uiState.store.orderableShopId ?: 0,
                                        isOrderableShop = uiState.isOrderableShop
                                    )
                                )
                            },
                            navigateToDetailInfo = { selectedInfo ->
                                navigateToDetailInfo(selectedInfo)
                            }
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
                            .onSizeChanged { size: IntSize ->
                                menuCategoryHeight.value = size.height
                            }
                            .heightIn(min = 66.dp),
                        menuCategories = uiState.categories,
                        onCategoryClicked = { categoryId ->
                            viewModel.clickMenuCategory(categoryId)
                            rememberState.collapseToolbar(
                                state = rememberState
                            )
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
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }

            KoinStoreTopAppBar(
                title = uiState.store.name,
                onNavigationIconClick = {
                    navigateToBack()
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = viewModel::navigateToCart) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null
                            )
                        }
                        if (uiState.cartItemCount > 0) {
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
                    }
                },
                overlayAlpha = overlayAlpha,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                )
            ) {
                StoreDetailImage(
                    modifier = Modifier
                        .heightIn(
                            rememberState.toolbarMinHeight,
                            rememberState.toolbarMaxHeight + statusBarHeight
                        )
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
        if (uiState.cart.items.isNotEmpty() && uiState.cart.orderableShopId == uiState.store.orderableShopId) {
            OrderBottomBar(
                itemCount = uiState.cart.items.sumOf { it.quantity },
                totalPrice = uiState.cart.totalAmount,
                isOrderEnabled = uiState.cartValidation == CartValidation.VALID,
                orderableMessage = if (uiState.cart.totalAmount >= uiState.minimumOrderAmount) stringResource(R.string.store_order_can_delivery) else stringResource(R.string.store_order_cant_delivery),
                navigateToCart = navigateToCart
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: StoreDetailSideEffect,
    navigateToCart: () -> Unit = {}
) {
    when (sideEffect) {
        StoreDetailSideEffect.NavigateToCart -> {
            navigateToCart()
        }
    }
}
