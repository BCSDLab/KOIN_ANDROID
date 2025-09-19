package `in`.koreatech.koin.feature.store.orders

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_HOME
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinOrdersFilterChip
import `in`.koreatech.koin.feature.store.component.KoinOrdersResetChip
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.enums.OrderHistoryStatus
import `in`.koreatech.koin.feature.store.enums.OrderInProgressStatus
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.model.OrderInProgressData
import `in`.koreatech.koin.feature.store.orders.component.FilterOverlay
import `in`.koreatech.koin.feature.store.orders.component.OrderHistoryCard
import `in`.koreatech.koin.feature.store.orders.component.OrderInProgressCard
import `in`.koreatech.koin.feature.store.orders.component.OrdersTabRow
import `in`.koreatech.koin.feature.store.orders.component.SearchBar
import java.time.LocalDate
import java.time.LocalTime
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = { },
    navigateToReview: (Int) -> Unit = { },
    navigateToReorder: (Int) -> Unit = { },
    onBackPressed: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onSearchCancel()
            viewModel.resetFilter()
        }
    }

    BackHandler(
        enabled = uiState.isSearching
    ) {
        viewModel.onSearchCancel()
    }

    viewModel.collectSideEffect {
        handleSideEffect(it, navigateToCart)
    }

    LaunchedEffect(Unit) {
        viewModel.getUserType()
    }

    LaunchedEffect(Unit) {
        viewModel.getOrderInProgressData()
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.filters }
            .collect {
                viewModel.getNewOrderHistoryData()
            }
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            KoinStoreTopAppBar(
                title = stringResource(R.string.store_title_home_history),
                onNavigationIconClick = {
                    onBackPressed()
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                )
            )

            OrdersTabRow(
                title = listOf(
                    stringResource(R.string.order_history_topbar),
                    stringResource(R.string.order_ongoing_topbar)
                ),
                selectedTabIndex = uiState.selectedTabIndex,
                onSelected = viewModel::onTabSelected
            )

            when (uiState.selectedTabIndex) {
                0 ->
                    OrderHistoryScreen(
                        filters = uiState.filters,
                        orderHistories = uiState.orderHistories,
                        isSearching = uiState.isSearching,
                        searchQuery = uiState.searchQuery,
                        getOrderHistoryData = viewModel::getOrderHistoryData,
                        onSearchStart = viewModel::onSearchStart,
                        onSearchCancel = viewModel::onSearchCancel,
                        onSearchDone = viewModel::onSearchDone,
                        onQueryChanged = viewModel::onSearchQueryChanged,
                        openFilterOverlay = viewModel::openFilterOverlay,
                        resetFilter = viewModel::resetFilter,
                        onWriteReviewClick = navigateToReview,
                        onReorderClick = navigateToReorder
                    )
                1 ->
                    OrderInProgressScreen(
                        orderInProgress = uiState.orderInProgress,
                        toOrderHistories = viewModel::onTabSelected
                    )
            }
        }

        if (uiState.isFilterSelecting) {
            FilterOverlay(
                filters = uiState.filters,
                onClose = viewModel::closeFilterOverlay,
                onApply = viewModel::applyFilter
            )
        }
    }
}

private fun handleSideEffect(
    sideEffect: OrderSideEffect,
    navigateToCart: () -> Unit
) {
    when (sideEffect) {
        OrderSideEffect.NavigateToCart -> {
            navigateToCart()
        }
    }
}

@Composable
fun OrderHistoryScreen(
    filters: OrderFilter,
    orderHistories: List<OrderHistoryData>,
    modifier: Modifier = Modifier,
    isSearching: Boolean = false,
    searchQuery: String = "",
    getOrderHistoryData: () -> Unit = { },
    onSearchStart: () -> Unit = { },
    onSearchCancel: () -> Unit = { },
    onSearchDone: () -> Unit = { },
    onQueryChanged: (String) -> Unit = { },
    resetFilter: () -> Unit = { },
    openFilterOverlay: () -> Unit = { },
    onWriteReviewClick: (Int) -> Unit = { },
    onReorderClick: (Int) -> Unit = { }
) {
    if (orderHistories.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box {
                    Icon(
                        modifier = Modifier.width(96.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_bbiko_sleep),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.TopStart),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_sleep_effect),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = stringResource(R.string.no_history_orders),
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500
                )
            }
        }
    } else {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleIndex ->
                    if (lastVisibleIndex == orderHistories.size - 1) {
                        getOrderHistoryData()
                    }
                }
        }

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onQueryChanged,
                    modifier = Modifier.weight(1f),
                    hint = stringResource(R.string.order_history_searchbar_hint),
                    onFocused = onSearchStart,
                    onDone = onSearchDone
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.clickable { onSearchCancel() },
                    text = stringResource(R.string.cancel),
                    style = RebrandKoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!filters.period.isDefault or !filters.type.isDefault or !filters.status.isDefault) {
                    item {
                        KoinOrdersResetChip(
                            onClick = { resetFilter() }
                        )
                    }
                }
                item {
                    KoinOrdersFilterChip(
                        text = stringResource(filters.period.stringRes),
                        isSelected = !filters.period.isDefault,
                        onClick = { openFilterOverlay() }
                    )
                }
                item {
                    KoinOrdersFilterChip(
                        text = stringResource(R.string.bullet_separator, stringResource(filters.type.stringRes), stringResource(filters.status.stringRes)),
                        isSelected = !filters.type.isDefault or !filters.status.isDefault,
                        onClick = { openFilterOverlay() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orderHistories) { orderdata ->
                        OrderHistoryCard(
                            orderdata = orderdata,
                            onDetailClick = { },
                            onWriteReviewClick = { onWriteReviewClick(orderdata.id) },
                            onReorderClick = { onReorderClick(orderdata.id) }
                        )
                    }
                }

                if (isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryPreview2() {
    val orderHistories: List<OrderHistoryData> = listOf(
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderHistoryStatus.CANCELED,
            orderDate = LocalDate.of(2025, 9, 5),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            openStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        ),
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderHistoryStatus.DELIVERED,
            orderDate = LocalDate.of(2025, 9, 5),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            openStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        )
    )

    OrderHistoryScreen(
        OrderFilter(
            period = PeriodOption.NONE,
            type = TypeOption.NONE,
            status = StatusOption.NONE
        ),
        orderHistories = orderHistories
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryPreview3() {
    val orderHistories: List<OrderHistoryData> = listOf(
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderHistoryStatus.CANCELED,
            orderDate = LocalDate.of(2025, 9, 5),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            openStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        ),
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderHistoryStatus.DELIVERED,
            orderDate = LocalDate.of(2025, 9, 5),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            openStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        )
    )

    OrderHistoryScreen(
        OrderFilter(
            period = PeriodOption.NONE,
            type = TypeOption.NONE,
            status = StatusOption.NONE
        ),
        orderHistories = orderHistories,
        isSearching = true
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryPreview4() {
    OrderHistoryScreen(
        OrderFilter(
            period = PeriodOption.NONE,
            type = TypeOption.NONE,
            status = StatusOption.NONE
        ),
        orderHistories = listOf()
    )
}

@Composable
fun OrderInProgressScreen(
    orderInProgress: List<OrderInProgressData>,
    modifier: Modifier = Modifier,
    toOrderHistories: (Int) -> Unit = { }
) {
    if (orderInProgress.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box {
                    Icon(
                        modifier = Modifier.width(96.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_bbiko_sleep),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.TopStart),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_sleep_effect),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = stringResource(R.string.no_ongoing_orders),
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500
                )

                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = RebrandKoinTheme.shapes.medium,
                            ambientColor = RebrandKoinTheme.colors.neutral400,
                            spotColor = RebrandKoinTheme.colors.neutral500
                        )
                        .background(
                            color = RebrandKoinTheme.colors.neutral0,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                        .clickable { toOrderHistories(0) }
                ) {
                    Text(
                        text = stringResource(R.string.goto_order_history),
                        style = RebrandKoinTheme.typography.bold13,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orderInProgress) { orderdata ->
                OrderInProgressCard(
                    orderdata = orderdata
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderInProgressPreview() {
    val orderInProgress = listOf(
        OrderInProgressData(
            id = 1,
            paymentId = 2,
            orderType = TypeOption.TAKE_OUT,
            estimatedAt = LocalTime.of(20, 32),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            orderableShopName = "맛있는 족발 - 병천점",
            orderStatus = OrderInProgressStatus.COOKING,
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        ),
        OrderInProgressData(
            id = 1,
            paymentId = 2,
            orderType = TypeOption.DELIVERY,
            estimatedAt = LocalTime.of(20, 32),
            orderableShopThumbnail = "https://example.com/store_thumbnail.jpg",
            orderableShopName = "맛있는 족발 - 병천점",
            orderStatus = OrderInProgressStatus.COOKING,
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            totalAmount = 32500
        )
    )

    OrderInProgressScreen(orderInProgress)
}

@Preview(showBackground = true)
@Composable
private fun OrderInProgressEmptyPreview() {
    val orderInProgress = listOf<OrderInProgressData>()

    OrderInProgressScreen(orderInProgress)
}
