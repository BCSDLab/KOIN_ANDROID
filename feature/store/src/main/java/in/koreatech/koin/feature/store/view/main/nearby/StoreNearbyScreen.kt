package `in`.koreatech.koin.feature.store.view.main.nearby

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.OpenStatus
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreCard
import `in`.koreatech.koin.feature.store.component.KoinStoreCategoryItem
import `in`.koreatech.koin.feature.store.component.KoinStoreFilterChip
import `in`.koreatech.koin.feature.store.component.KoinStoreOrderChip
import `in`.koreatech.koin.feature.store.component.MinOrderSliderBottomSheet
import `in`.koreatech.koin.feature.store.component.SearchBarFake
import `in`.koreatech.koin.feature.store.component.SortBottomSheet
import `in`.koreatech.koin.feature.store.enums.FilterBadge
import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.enums.minimumPriceOptions
import `in`.koreatech.koin.feature.store.model.LocalShop
import `in`.koreatech.koin.feature.store.model.LocalStoreCategories
import kotlinx.coroutines.flow.combine
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreNearbyScreen(
    modifier: Modifier = Modifier,
    categoryId: Int = 1,
    viewModel: StoreNearbyViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit = { },
    navigateToCart: () -> Unit = { },
    navigateToSearch: () -> Unit = { },
    onBackPressed: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.categoryId == -1) {
            viewModel.onCategoryChange(categoryId)
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

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.store_title_home_nearby),
            onNavigationIconClick = {
                onBackPressed()
            },
            actions = {
                Image(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(24.dp)
                        .noRippleClickable { navigateToCart() },
                    painter = painterResource(R.drawable.ic_shopping_cart),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xFF1C1B1F))
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0XFFF2F2F2)
            )
        )

        StoreNearbyScreen(
            isLoading = uiState.isLoading,
            showOrderOptions = uiState.showOrderOptions,
            storeList = uiState.orderableShops,
            categoryId = uiState.categoryId,
            storeCategories = uiState.storeCategories,
            selectedOrderOption = uiState.selectedOrderOption,
            selectedStoreFilter = uiState.selectedStoreFilter,
            selectedMinimumPriceOption = uiState.selectedMinimumPriceOption,
            showMinimumPriceOptions = uiState.showMinimumPriceOptions,
            navigateToDetail = navigateToDetail,
            navigateToSearch = navigateToSearch,
            onCategoryChange = viewModel::onCategoryChange,
            onShowOrderOptionsChange = viewModel::onShowOrderOptionsChange,
            onSelectedOrderOptionChange = viewModel::onSelectedOrderOptionChange,
            onSelectedStoreFilterChange = viewModel::onSelectedStoreFilterChange,
            onShowMinimumPriceOptionsChange = viewModel::onShowMinimumPriceOptionsChange,
            onSelectedMinimumPriceOptionChange = viewModel::onSelectedMinimumPriceOptionChange
        )
    }
}

@Composable
private fun StoreNearbyScreen(
    isLoading: Boolean,
    categoryId: Int,
    storeList: List<LocalShop>,
    storeCategories: List<LocalStoreCategories>,
    selectedOrderOption: OrderOption,
    selectedStoreFilter: List<StoreFilter>,
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
                CircularProgressIndicator()
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

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                itemsIndexed(storeCategories) { index, category ->
                    KoinStoreCategoryItem(
                        categoryName = category.name,
                        categoryIcon = rememberAsyncImagePainter(
                            model = category.imageUrl
                        ),
                        isSelected = index + 1 == categoryId,
                        onClick = {
                            if (index + 1 == categoryId) return@KoinStoreCategoryItem
                            onCategoryChange(index + 1) // Category IDs start from 1
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

                KoinStoreOrderChip(
                    modifier = Modifier.fillMaxHeight(),
                    text = stringResource(selectedOrderOption.stringResId)
                ) {
                    onShowOrderOptionsChange(true)
                }

                Spacer(modifier = Modifier.width(8.dp))

                StoreFilter.IS_OPEN.let {
                    KoinStoreFilterChip(
                        modifier = Modifier.fillMaxHeight(),
                        text = stringResource(it.stringResId),
                        icon = painterResource(it.iconResId),
                        isSelected = selectedStoreFilter.contains(it),
                        onClick = {
                            onSelectedStoreFilterChange(it)
                        }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    items(storeList) {
                        KoinStoreCard(
                            modifier = Modifier.fillMaxWidth(),
                            storeName = it.name,
                            storeAverageRating = it.ratingAverage.toString(),
                            storeReviewCount = it.reviewCount,
                            storeDeliveryFee = it.minimumDeliveryTip.toString(),
                            storeImageUrl = it.imageUrls.firstOrNull() ?: "",
                            isOpen = it.isOpen,
                            filterBadgeList = it.filterBadgeList
                        ) {
                            navigateToDetail(it.shopId)
                        }
                    }
                }
            }

            if (showOrderOptions) {
                SortBottomSheet(
                    currentIndex = selectedOrderOption.ordinal,
                    options = OrderOption.entries.map { context.getString(it.stringResId) },
                    onSelect = { index ->
                        onSelectedOrderOptionChange(OrderOption.entries[index])
                        onShowOrderOptionsChange(false)
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
private fun StoreNearbyScreenPreview() {
    RebrandKoinTheme {
        StoreNearbyScreen(
            isLoading = false,
            showOrderOptions = false,
            categoryId = 0,
            storeList = listOf(
                LocalShop(
                    shopId = 1,
                    orderableShopId = 1,
                    name = "Sample Store",
                    filterBadgeList = listOf(
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
                    imageUrls = listOf("https://example.com/store.jpg"),
                    open = listOf(
                        LocalShop.LocalOrderStoreShopsOpen(
                            dayOfWeek = 1,
                            closed = false,
                            openTime = "09:00",
                            closeTime = "21:00"
                        )
                    ),
                    openStatus = OpenStatus.OPERATING
                )
            ),
            storeCategories = listOf(
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
            selectedStoreFilter = listOf(StoreFilter.IS_OPEN),
            selectedMinimumPriceOption = MinimumPriceOption.ALL,
            showMinimumPriceOptions = false
        )
    }
}
