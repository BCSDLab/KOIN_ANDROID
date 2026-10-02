package `in`.koreatech.koin.feature.store.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.EventUtils
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.core.nestedscroll.KoinNestedScrollHeaderState
import `in`.koreatech.koin.core.nestedscroll.rememberKoinNestedScrollConnection
import `in`.koreatech.koin.core.nestedscroll.rememberKoinNestedScrollHeaderState
import `in`.koreatech.koin.core.util.pxToDp
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_DETAIL_MAIN
import `in`.koreatech.koin.feature.store.LocalDeliveryDeveloperOption
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.OrderBottomBar
import `in`.koreatech.koin.feature.store.component.dialog.StoreImageDialog
import `in`.koreatech.koin.feature.store.detail.component.CallDialog
import `in`.koreatech.koin.feature.store.detail.component.MenuCategoryChips
import `in`.koreatech.koin.feature.store.detail.component.StoreDetailImage
import `in`.koreatech.koin.feature.store.detail.component.StoreDetailInfo
import `in`.koreatech.koin.feature.store.detail.component.menuListSection
import `in`.koreatech.koin.feature.store.enums.CartValidation
import `in`.koreatech.koin.feature.store.enums.StoreDetailInfoType
import `in`.koreatech.koin.feature.store.model.MenuCategoryModel
import `in`.koreatech.koin.feature.store.model.ShopInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Composable
fun StoreDetailScreen(
    modifier: Modifier = Modifier,
    isCartAdded: Boolean = false,
    isCartModified: Boolean = false,
    viewModel: StoreDetailViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: (selectedInfo: String) -> Unit = {},
    navigateToNotice: (selectedInfo: String) -> Unit = {},
    navigateToReview: (StoreNavigationData, String) -> Unit = { _, _ -> },
    navigateToMenuInfo: (menuId: Int) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val navigator = rememberNavigator()

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            viewModel.setCallDialogState(true)
        } else {
            viewModel.intent {
                postSideEffect(StoreDetailSideEffect.PermissionDenied)
            }
        }
    }

    val pagerState = rememberPagerState(0, 0f) {
        uiState.store.imageUrls?.size ?: 0
    }

    val rememberState = rememberKoinNestedScrollHeaderState()
    val listState = rememberLazyListState()
    val menuCategoryHeight = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(
            sideEffect = sideEffect,
            context = context,
            checkPermission = {
                permissionLauncher.launch(Manifest.permission.CALL_PHONE)
            },
            navigateToCart = navigateToCart,
            scrollToMenuCategory = { categoryId ->
                coroutineScope.launch {
                    rememberState.snapOffset(-rememberState.range)
                    listState.animateScrollToItem(uiState.categories.indexOfFirst { it.menuGroupId == categoryId } + 2, -menuCategoryHeight.value)
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        EventLogger.logScreenName("ShopDetailActivity") // DA requirement
    }

    LaunchedEffect(Unit) {
        EventUtils.getElapsedTimeAndReset() // Reset elapsed time
    }

    LaunchedEffect(isCartModified) {
        snapshotFlow { isCartModified }
            .distinctUntilChanged()
            .onEach {
                if (it && uiState.isLogin) {
                    viewModel.getCart(uiState.cartType)
                }
            }
            .launchIn(coroutineScope)
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

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                EventLogger.logScrollEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
                    uiState.store.name
                )
            }
    }

    LaunchedEffect(listState) {
        combine(
            snapshotFlow { listState.firstVisibleItemIndex },
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastIndex }
        ) { index, _ ->
            index + 1
        }.collect { index ->
            val visibleCategory = if (!listState.isScrolledToTheEnd()) {
                uiState.categories.getOrNull(index - 2)
            } else {
                uiState.categories.lastOrNull()
            }
            visibleCategory?.let {
                viewModel.changeCategory(it.menuGroupId)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect {
                EventLogger.logSwipeEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_PICTURE_SWIPE,
                    uiState.store.name
                )
            }
    }

    BackHandler {
        navigateToBack()
        EventLogger.logClickEvent(
            EventAction.BUSINESS,
            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
            uiState.store.name,
            EventExtra(AnalyticsConstant.DURATION_TIME, "${EventUtils.getElapsedTime()}")
        )
    }

    StoreDetailScreen(
        uiState = uiState,
        pagerState = pagerState,
        listState = listState,
        nestedScrollHeaderState = rememberState,
        modifier = modifier,
        navigateToCart = navigateToCart,
        navigateToBack = navigateToBack,
        navigateToDetailInfo = navigateToDetailInfo,
        navigateToNotice = navigateToNotice,
        navigateToReview = navigateToReview,
        navigateToMenuInfo = navigateToMenuInfo,
        onCall = { phoneNumber ->
            context.startActivity(Intent(Intent.ACTION_CALL, "tel:$phoneNumber".toUri()))
            viewModel.setCallDialogState(false)
        },
        onCallDialogDismiss = { viewModel.setCallDialogState(false) },
        onImageClick = { viewModel.setImageDialogState(true) },
        onImageDialogDismiss = { viewModel.setImageDialogState(false) },
        onSignIn = {
            navigator.navigateToSignIn(
                context,
                "$DEEPLINK_STORE_DETAIL_MAIN/${uiState.storeId}/${uiState.isOrderableShop}"
            ).apply {
                context.startActivity(this)
            }
        },
        onSignInDialogDismiss = viewModel::hideSignInDialog,
        onCallClick = {
            viewModel.intent {
                postSideEffect(StoreDetailSideEffect.CheckCallPermission)
            }
        },
        onCategoryClick = viewModel::clickMenuCategory,
        onMenuCategoryHeightChanged = { menuCategoryHeight.value = it },
        onCartClick = viewModel::navigateToCart
    )
}

@Composable
private fun StoreDetailScreen(
    uiState: StoreDetailState,
    pagerState: PagerState,
    listState: LazyListState,
    nestedScrollHeaderState: KoinNestedScrollHeaderState,
    modifier: Modifier = Modifier,
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {},
    navigateToDetailInfo: (selectedInfo: String) -> Unit = {},
    navigateToNotice: (selectedInfo: String) -> Unit = {},
    navigateToReview: (StoreNavigationData, String) -> Unit = { _, _ -> },
    navigateToMenuInfo: (menuId: Int) -> Unit = {},
    onCall: (String) -> Unit = {},
    onCallDialogDismiss: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onImageDialogDismiss: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onSignInDialogDismiss: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
    onMenuCategoryHeightChanged: (Int) -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    val nestedScrollConnection = rememberKoinNestedScrollConnection(nestedScrollHeaderState)
    val currentToolbarHeightDp = nestedScrollHeaderState.currentHeaderHeightDp()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val onMenuClick = remember(uiState.isOrderableShop) {
        if (uiState.isOrderableShop) {
            navigateToMenuInfo
        } else {
            {}
        }
    }

    StoreDetailDialogs(
        showCallDialog = uiState.showCallDialog,
        showImageDialog = uiState.showImageDialog,
        showSignInDialog = uiState.showSignInDialog,
        phoneNumber = uiState.shopDescription.phone,
        storeName = uiState.store.name,
        imageUrls = uiState.store.imageUrls ?: persistentListOf(),
        onCall = onCall,
        onCallDialogDismiss = onCallDialogDismiss,
        onImageDialogDismiss = onImageDialogDismiss,
        onSignIn = onSignIn,
        onSignInDialogDismiss = onSignInDialogDismiss
    )

    StoreDetailLoading(isLoading = uiState.isLoading)

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
            StoreDetailMenuList(
                uiState = StoreDetailMenuUiState(
                    store = uiState.store,
                    storeReview = uiState.storeReview,
                    shopDescription = uiState.shopDescription,
                    noticePreview = uiState.noticePreview,
                    isOrderableShop = uiState.isOrderableShop,
                    categories = uiState.categories
                ),
                listState = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = nestedScrollHeaderState.headerCollapsedHeightPx.pxToDp + statusBarHeight)
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
                navigateToReview = navigateToReview,
                navigateToDetailInfo = navigateToDetailInfo,
                navigateToNotice = navigateToNotice,
                onCallClick = onCallClick,
                onCategoryClick = onCategoryClick,
                onMenuCategoryHeightChanged = onMenuCategoryHeightChanged,
                onMenuClick = onMenuClick
            )

            StoreDetailTopAppBar(
                storeName = uiState.store.name,
                imageUrls = uiState.store.imageUrls ?: persistentListOf(),
                cartItemCount = uiState.cartItemCount,
                pagerState = pagerState,
                nestedScrollHeaderState = nestedScrollHeaderState,
                statusBarHeight = statusBarHeight,
                onBackClick = navigateToBack,
                onCartClick = onCartClick,
                onImageClick = onImageClick
            )
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

@Composable
private fun StoreDetailDialogs(
    showCallDialog: Boolean,
    showImageDialog: Boolean,
    showSignInDialog: Boolean,
    phoneNumber: String,
    storeName: String,
    imageUrls: ImmutableList<String>,
    onCall: (String) -> Unit,
    onCallDialogDismiss: () -> Unit,
    onImageDialogDismiss: () -> Unit,
    onSignIn: () -> Unit,
    onSignInDialogDismiss: () -> Unit
) {
    if (showCallDialog) {
        CallDialog(
            phoneNumber = phoneNumber,
            call = {
                onCall(it)
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CALL,
                    storeName,
                    EventExtra(AnalyticsConstant.DURATION_TIME, "${EventUtils.getElapsedTimeAndReset()}")
                )
            },
            onDismissRequest = onCallDialogDismiss
        )
    }

    if (showImageDialog) {
        StoreImageDialog(
            imageUrls = imageUrls,
            onDismiss = onImageDialogDismiss
        )
    }

    if (showSignInDialog) {
        KoinStoreSignInDialog(
            onPositive = onSignIn,
            onNegative = onSignInDialogDismiss
        )
    }
}

@Composable
private fun StoreDetailLoading(isLoading: Boolean) {
    if (!isLoading) return

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

@Composable
private fun StoreDetailTopAppBar(
    storeName: String,
    imageUrls: ImmutableList<String>,
    cartItemCount: Int,
    pagerState: PagerState,
    nestedScrollHeaderState: KoinNestedScrollHeaderState,
    statusBarHeight: Dp,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onImageClick: () -> Unit
) {
    val overlayAlpha = nestedScrollHeaderState.progress()
    val currentToolbarHeightDp = nestedScrollHeaderState.currentHeaderHeightDp()

    KoinStoreTopAppBar(
        title = storeName,
        onNavigationIconClick = {
            onBackClick()
            EventLogger.logClickEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
                storeName,
                EventExtra(AnalyticsConstant.DURATION_TIME, "${EventUtils.getElapsedTime()}")
            )
        },
        actions = {
            if (!LocalDeliveryDeveloperOption.current) return@KoinStoreTopAppBar
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = onCartClick) {
                    Icon(
                        modifier = Modifier.size(24.dp).padding(1.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                        contentDescription = null
                    )
                }
                if (cartItemCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = (-5).dp, y = 5.dp)
                            .size(16.dp)
                            .background(RebrandKoinTheme.colors.primary500, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$cartItemCount",
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
        overlayAlpha = { overlayAlpha.value },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.store_detail_background)
        )
    ) {
        StoreDetailImage(
            modifier = Modifier
                .heightIn(
                    nestedScrollHeaderState.headerCollapsedHeightPx.pxToDp,
                    nestedScrollHeaderState.headerExpandedHeightPx.pxToDp + statusBarHeight
                )
                .fillMaxWidth()
                .graphicsLayer {
                    clip = true
                    translationY = -(nestedScrollHeaderState.headerExpandedHeightPx - currentToolbarHeightDp.value.toPx())
                    alpha = 1f - overlayAlpha.value
                }
                .clickable(onClick = onImageClick),
            imageUrls = imageUrls,
            pagerState = pagerState
        )
    }
}

@Composable
private fun StoreDetailMenuList(
    uiState: StoreDetailMenuUiState,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToReview: (StoreNavigationData, String) -> Unit = { _, _ -> },
    navigateToDetailInfo: (selectedInfo: String) -> Unit = {},
    navigateToNotice: (selectedInfo: String) -> Unit = {},
    onCallClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
    onMenuCategoryHeightChanged: (Int) -> Unit = {},
    onMenuClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        item {
            Column(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                StoreDetailInfo(
                    storeInfo = uiState.store,
                    storeReview = uiState.storeReview,
                    storeDescriptionModel = uiState.shopDescription,
                    noticePreview = uiState.noticePreview,
                    isOrderableShop = uiState.isOrderableShop,
                    phoneNumber = uiState.shopDescription.phone,
                    navigateToReview = {
                        navigateToReview(
                            StoreNavigationData(
                                shopId = uiState.store.shopId,
                                orderableShopId = uiState.store.orderableShopId ?: 0,
                                isOrderableShop = uiState.isOrderableShop
                            ),
                            uiState.store.name
                        )
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW,
                            uiState.store.name
                        )
                    },
                    navigateToDetailInfo = { selectedInfo ->
                        navigateToDetailInfo(selectedInfo)
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_INFO,
                            uiState.store.name
                        )
                    },
                    navigateToNotice = {
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_BENEFIT_ENTRY,
                            uiState.store.name
                        )
                        navigateToNotice(StoreDetailInfoType.EVENT.name)
                    },
                    call = { onCallClick() }
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
                        onMenuCategoryHeightChanged(size.height)
                    }
                    .heightIn(min = 66.dp),
                menuCategories = uiState.categories,
                onCategoryClicked = onCategoryClick
            )
        }
        uiState.categories.fastForEach { category ->
            menuListSection(
                category = category,
                menus = category.menus,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onMenuClick = {
                    onMenuClick(it)
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_DETAIL_VIEW,
                        uiState.store.name
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Immutable
private data class StoreDetailMenuUiState(
    val store: ShopInfoModel,
    val storeReview: StoreReview,
    val shopDescription: StoreDescriptionModel,
    val noticePreview: String?,
    val isOrderableShop: Boolean,
    val categories: ImmutableList<MenuCategoryModel>
)

fun handleSideEffect(
    sideEffect: StoreDetailSideEffect,
    context: Context,
    checkPermission: () -> Unit = {},
    navigateToCart: () -> Unit = {},
    scrollToMenuCategory: (categoryId: Int) -> Unit = {}
) {
    when (sideEffect) {
        StoreDetailSideEffect.NavigateToCart -> {
            navigateToCart()
        }

        StoreDetailSideEffect.CheckCallPermission -> {
            checkPermission()
        }

        StoreDetailSideEffect.PermissionDenied -> {
            Toast.makeText(context, context.getString(R.string.call_permission_denied_message), Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }

        is StoreDetailSideEffect.ScrollToMenuCategory -> {
            scrollToMenuCategory(sideEffect.categoryId)
        }
    }
}

fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
