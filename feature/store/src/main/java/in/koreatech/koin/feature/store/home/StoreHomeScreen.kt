package `in`.koreatech.koin.feature.store.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.EventUtils
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import `in`.koreatech.koin.domain.model.store.OpenStatus
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_HOME
import `in`.koreatech.koin.feature.store.LocalDeliveryDeveloperOption
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreCard
import `in`.koreatech.koin.feature.store.component.KoinStoreCategoryItem
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.component.KoinStoreFloatingButton
import `in`.koreatech.koin.feature.store.component.KoinStoreMinimumPriceChip
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.MinOrderSliderBottomSheet
import `in`.koreatech.koin.feature.store.component.SearchBarFake
import `in`.koreatech.koin.feature.store.component.SortBottomSheet
import `in`.koreatech.koin.feature.store.enums.FilterBadge
import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.enums.minimumPriceOptions
import `in`.koreatech.koin.feature.store.enums.storeFilters
import `in`.koreatech.koin.feature.store.model.LocalShop
import `in`.koreatech.koin.feature.store.model.LocalStoreCategories
import `in`.koreatech.koin.feature.store.model.OrderStatus
import `in`.koreatech.koin.feature.store.model.OrderType
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreHomeScreen(
    modifier: Modifier = Modifier,
    categoryId: Int = 1,
    viewModel: StoreHomeViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit = { },
    navigateToCart: () -> Unit = { },
    navigateToSearch: () -> Unit = { },
    navigateToOrderResult: (orderId: Int) -> Unit = { },
    onBackPressed: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, navigateToCart)
    }

    LaunchedEffect(Unit) {
        if (uiState.categoryId == -1) {
            viewModel.onCategoryChange(categoryId)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.isLoggedIn }.collect {
            if (it) {
                viewModel.getOrderInProgress()
            }
        }
    }

    LaunchedEffect(Unit) {
        combine(
            snapshotFlow { uiState.selectedStoreFilter },
            snapshotFlow { uiState.selectedOrderOption },
            snapshotFlow { uiState.categoryId },
            snapshotFlow { uiState.selectedMinimumPriceOption }
        ) { _, _, _, _ -> }.collect {
            viewModel.fetchData()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getUserType()
    }

    if (uiState.showSignInDialog) {
        KoinStoreSignInDialog(
            onPositive = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "koin://login/login?link=$DEEPLINK_STORE_MAIN_HOME".toUri()
                }.apply {
                    context.startActivity(this)
                }
            },
            onNegative = viewModel::hideSignInDialog
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        KoinStoreTopAppBar(
            title = stringResource(R.string.store_title_home_order),
            onNavigationIconClick = {
                onBackPressed()
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CATEGORIES_BACK,
                    "",
                    EventExtra(AnalyticsConstant.PREVIOUS_PAGE, uiState.storeCategories.first { it.id == uiState.categoryId }.name),
                    EventExtra(AnalyticsConstant.CURRENT_PAGE, "메인"),
                    EventExtra(AnalyticsConstant.DURATION_TIME, "${EventUtils.getElapsedTime()}")
                )
            },
            actions = {
                if (!LocalDeliveryDeveloperOption.current) return@KoinStoreTopAppBar
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
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.store_detail_background)
            )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            StoreHomeScreen(
                isLoading = uiState.isLoading,
                showOrderOptions = uiState.showOrderOptions,
                storeList = uiState.orderableShops,
                initCategoryId = categoryId,
                categoryId = uiState.categoryId,
                storeCategories = uiState.storeCategories,
                selectedOrderOption = uiState.selectedOrderOption,
                selectedStoreFilter = uiState.selectedStoreFilter,
                selectedMinimumPriceOption = uiState.selectedMinimumPriceOption,
                showMinimumPriceOptions = uiState.showMinimumPriceOptions,
                navigateToDetail = navigateToDetail,
                navigateToSearch = {
                    navigateToSearch()
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CATEGORIES_SEARCH,
                        "search in ${uiState.storeCategories.first { it.id == categoryId }.name}"
                    )
                },
                onCategoryChange = viewModel::onCategoryChange,
                onShowOrderOptionsChange = viewModel::onShowOrderOptionsChange,
                onSelectedOrderOptionChange = viewModel::onSelectedOrderOptionChange,
                onSelectedStoreFilterChange = viewModel::onSelectedStoreFilterChange,
                onShowMinimumPriceOptionsChange = viewModel::onShowMinimumPriceOptionsChange,
                onSelectedMinimumPriceOptionChange = viewModel::onSelectedMinimumPriceOptionChange
            )

            uiState.orderInProgress?.apply {
                if (orderStatus != OrderStatus.NONE) {
                    KoinStoreFloatingButton(
                        modifier = Modifier
                            .zIndex(1f)
                            .padding(bottom = 12.dp),
                        text = when (orderStatus) {
                            OrderStatus.CONFIRMING -> stringResource(R.string.store_fab_confirming)
                            OrderStatus.DELIVERING,
                            OrderStatus.PACKAGED -> stringResource(
                                when (orderType) {
                                    OrderType.DELIVERY -> R.string.store_fab_delivery_eta
                                    OrderType.TAKE_OUT -> R.string.store_fab_takeout_eta
                                },
                                estimatedAt?.format(DateTimeFormatter.ofPattern("a h시 m분")) ?: stringResource(R.string.store_fab_eta_unavailable)
                            )

                            OrderStatus.NONE,
                            OrderStatus.COOKING,
                            OrderStatus.PICKED_UP,
                            OrderStatus.DELIVERED,
                            OrderStatus.CANCELED -> ""
                        },
                        storeName = shopName,
                        onClick = {
                            navigateToOrderResult(paymentId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreHomeScreen(
    isLoading: Boolean,
    categoryId: Int,
    initCategoryId: Int,
    storeList: ImmutableList<LocalShop>,
    storeCategories: ImmutableList<LocalStoreCategories>,
    selectedOrderOption: OrderOption,
    selectedStoreFilter: ImmutableList<StoreFilter>,
    selectedMinimumPriceOption: MinimumPriceOption,
    showOrderOptions: Boolean,
    showMinimumPriceOptions: Boolean,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit = { },
    navigateToSearch: () -> Unit = { },
    onCategoryChange: (Int) -> Unit = { },
    onShowOrderOptionsChange: (Boolean) -> Unit = { },
    onSelectedOrderOptionChange: (OrderOption) -> Unit = { },
    onSelectedStoreFilterChange: (StoreFilter) -> Unit = { },
    onSelectedMinimumPriceOptionChange: (MinimumPriceOption) -> Unit = { },
    onShowMinimumPriceOptionsChange: (Boolean) -> Unit = { }
) {
    val context = LocalContext.current
    val categoryListState = rememberLazyListState()
    val shopListState = rememberLazyListState()

    LaunchedEffect(categoryId) {
        if (categoryId != -1) {
            categoryListState.animateScrollToItem(storeCategories.map { it.id }.indexOf(categoryId))
            shopListState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(selectedOrderOption) {
        shopListState.animateScrollToItem(0)
    }

    LaunchedEffect(storeCategories) {
        if (storeCategories.isEmpty()) return@LaunchedEffect
        snapshotFlow { categoryListState.isScrollInProgress }
            .filter { it }
            .collect {
                storeCategories.firstOrNull { it.id == categoryId }.let {
                    EventLogger.logScrollEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CATEGORIES,
                        "scroll in ${it?.name}"
                    )
                }
            }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                KoinStoreProgressIndicator(
                    modifier = Modifier.size(150.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBarFake(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                navigateToSearch()
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = categoryListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                itemsIndexed(
                    storeCategories,
                    key = { index, category ->
                        category.id
                    }
                ) { index, category ->
                    KoinStoreCategoryItem(
                        categoryName = category.name,
                        categoryIcon = rememberAsyncImagePainter(
                            model = category.imageUrl,
                            imageLoader = KoinCoilImageLoader.getImageLoader(context)
                        ),
                        isSelected = storeCategories[index].id == categoryId,
                        onClick = remember(key1 = category.id) {
                            {
                                onCategoryChange(storeCategories[index].id)
                                EventLogger.logClickEvent(
                                    EventAction.BUSINESS,
                                    AnalyticsConstant.Label.MAIN_SHOP_BENEFIT,
                                    storeCategories.first { it.id == initCategoryId }.name,
                                    EventExtra(AnalyticsConstant.PREVIOUS_PAGE, storeCategories.first { it.id == categoryId }.name),
                                    EventExtra(AnalyticsConstant.CURRENT_PAGE, storeCategories.first { it.id == index + 1 }.name),
                                    EventExtra(
                                        AnalyticsConstant.DURATION_TIME,
                                        EventUtils.getElapsedTimeAndReset().toString()
                                    )
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                key(selectedOrderOption) {
                    KoinStoreChip(
                        modifier = Modifier.fillMaxHeight(),
                        text = stringResource(selectedOrderOption.stringResId),
                        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                            textColor = RebrandKoinTheme.colors.primary500,
                            borderWidth = 1.dp,
                            borderColor = RebrandKoinTheme.colors.primary500,
                            elevation = 0.dp
                        ),
                        trailingIcon = rememberVectorPainter(ImageVector.vectorResource(R.drawable.ic_store_arrow_down)),
                        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
                            iconColor = RebrandKoinTheme.colors.primary500
                        )
                    ) {
                        onShowOrderOptionsChange(true)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                storeFilters.forEach {
                    key(it) {
                        val isSelected = selectedStoreFilter.contains(it)
                        KoinStoreChip(
                            modifier = Modifier.fillMaxHeight(),
                            text = stringResource(it.stringResId),
                            leadingIcon = rememberVectorPainter(ImageVector.vectorResource(it.iconResId)),
                            chipStyle = if (isSelected) {
                                KoinStoreChipDefaults.koinStoreChipStyle(
                                    elevation = 0.dp,
                                    containerColor = RebrandKoinTheme.colors.primary500,
                                    textColor = RebrandKoinTheme.colors.neutral0
                                )
                            } else {
                                KoinStoreChipDefaults.koinStoreChipStyle()
                            },
                            leadingIconStyle = if (isSelected) {
                                KoinStoreChipDefaults.koinStoreIconStyle(
                                    iconColor = RebrandKoinTheme.colors.neutral0
                                )
                            } else {
                                KoinStoreChipDefaults.koinStoreIconStyle()
                            },
                            onClick = remember(key1 = it) {
                                {
                                    onSelectedStoreFilterChange(it)
                                    if (isSelected) {
                                        EventLogger.logClickEvent(
                                            EventAction.BUSINESS,
                                            AnalyticsConstant.Label.SHOP_CAN,
                                            when (it) {
                                                StoreFilter.IS_OPEN -> "check_open_${storeCategories.first { categoryId == initCategoryId }.name}"
                                                StoreFilter.DELIVERY_AVAILABLE -> "check_delivery_${storeCategories.first { categoryId == initCategoryId }.name}"
                                                StoreFilter.TAKEOUT_AVAILABLE -> "check_takeout_${storeCategories.first { categoryId == initCategoryId }.name}"
                                                StoreFilter.FREE_DELIVERY_TIP -> "check_free_delivery_${storeCategories.first { categoryId == initCategoryId }.name}"
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                KoinStoreMinimumPriceChip(
                    modifier = Modifier.fillMaxHeight(),
                    minimumPrice = selectedMinimumPriceOption.price,
                    onClick = {
                        onShowMinimumPriceOptionsChange(true)
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_CAN,
                            "check_min_amount_${storeCategories.first { categoryId == initCategoryId }.name}"
                        )
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = shopListState
            ) {
                if (!isLoading && storeList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.aspectRatio(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_store_no_store),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            BasicText(
                                text = stringResource(R.string.store_list_empty),
                                style = RebrandKoinTheme.typography.bold18.copy(
                                    color = RebrandKoinTheme.colors.primary500
                                )
                            )
                            BasicText(
                                text = stringResource(R.string.store_list_empty_description),
                                style = RebrandKoinTheme.typography.regular14.copy(
                                    color = RebrandKoinTheme.colors.neutral600
                                )
                            )
                        }
                    }
                } else {
                    items(
                        storeList,
                        key = {
                            it.shopId
                        }
                    ) {
                        KoinStoreCard(
                            modifier = Modifier.fillMaxWidth(),
                            storeName = it.name,
                            storeAverageRating = it.ratingAverage.toString(),
                            storeReviewCount = it.reviewCount,
                            storeDeliveryFee = it.minimumDeliveryTip,
                            storeImageUrl = it.thumbnail,
                            isOpen = it.isOpen,
                            filterBadgeList = it.filterBadgeList,
                            onClick = remember(key1 = it.shopId) {
                                {
                                    navigateToDetail(it.orderableShopId)
                                    EventLogger.logClickEvent(
                                        EventAction.BUSINESS,
                                        AnalyticsConstant.Label.SHOP_CLICK,
                                        it.name,
                                        EventExtra(AnalyticsConstant.PREVIOUS_PAGE, storeCategories.first { categoryId == initCategoryId }.name),
                                        EventExtra(AnalyticsConstant.CURRENT_PAGE, it.name),
                                        EventExtra(
                                            AnalyticsConstant.DURATION_TIME,
                                            EventUtils.getElapsedTimeAndReset().toString()
                                        )
                                    )
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
                }
            }

            if (showOrderOptions) {
                SortBottomSheet(
                    currentIndex = selectedOrderOption.ordinal,
                    options = OrderOption.entries.map { context.getString(it.stringResId) },
                    onSelect = { index ->
                        onSelectedOrderOptionChange(OrderOption.entries[index])
                        onShowOrderOptionsChange(false)
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_CAN,
                            when (index) {
                                0 -> {
                                    "check_default_${storeCategories.first { it.id == categoryId }.name}"
                                }

                                1 -> {
                                    "check_review_${storeCategories.first { it.id == categoryId }.name}"
                                }

                                else -> {
                                    "check_star_${storeCategories.first { it.id == categoryId }.name}"
                                }
                            }
                        )
                    },
                    onClose = {
                        onShowOrderOptionsChange(false)
                    }
                )
            }

            if (showMinimumPriceOptions) {
                MinOrderSliderBottomSheet(
                    selectedIndex = minimumPriceOptions.indexOf(selectedMinimumPriceOption),
                    options = minimumPriceOptions.map { context.getString(it.stringRes) },
                    onSelected = { index ->
                        onSelectedMinimumPriceOptionChange(minimumPriceOptions[index])
                        onShowMinimumPriceOptionsChange(false)
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.MIN_AMOUNT_SET,
                            "${minimumPriceOptions[index]}"
                        )
                    },
                    onClose = {
                        onShowMinimumPriceOptionsChange(false)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreHomeScreenPreview() {
    RebrandKoinTheme {
        StoreHomeScreen(
            isLoading = false,
            showOrderOptions = false,
            categoryId = 0,
            initCategoryId = 0,
            storeList = persistentListOf(
                LocalShop(
                    shopId = 1,
                    orderableShopId = 1,
                    name = "Sample Store",
                    filterBadgeList = persistentListOf(
                        FilterBadge.PICKUP_AVAILABLE,
                        FilterBadge.DELIVERY_AVAILABLE,
                        FilterBadge.SERVICE
                    ),
                    minimumOrderAmount = 1000,
                    ratingAverage = 4.0,
                    reviewCount = 50,
                    minimumDeliveryTip = 200,
                    maximumDeliveryTip = 500,
                    isOpen = true,
                    categoryIds = listOf(0, 1),
                    images = listOf("https://example.com/store.jpg"),
                    thumbnail = "https://example.com/store_thumbnail.jpg",
                    openStatus = OpenStatus.OPERATING
                )
            ),
            storeCategories = persistentListOf(
                LocalStoreCategories(
                    id = 0,
                    name = "Category 1",
                    imageUrl = "https://example.com/category1.jpg"
                ),
                LocalStoreCategories(
                    id = 1,
                    name = "Category 2",
                    imageUrl = "https://example.com/category2.jpg"
                )
            ),
            selectedOrderOption = OrderOption.NONE,
            selectedStoreFilter = persistentListOf(StoreFilter.IS_OPEN),
            selectedMinimumPriceOption = MinimumPriceOption.ALL,
            showMinimumPriceOptions = false
        )
    }
}

private fun handleSideEffect(
    sideEffect: StoreHomeSideEffect,
    navigateToCart: () -> Unit
) {
    when (sideEffect) {
        StoreHomeSideEffect.NavigateToCart -> {
            navigateToCart()
        }
    }
}
