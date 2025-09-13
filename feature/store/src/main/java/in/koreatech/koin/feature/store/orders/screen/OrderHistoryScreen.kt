package `in`.koreatech.koin.feature.store.orders.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinOrdersFilterChip
import `in`.koreatech.koin.feature.store.component.KoinOrdersResetChip
import `in`.koreatech.koin.feature.store.enums.OrderHistoryStatus
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.orders.component.OrderHistoryCard
import `in`.koreatech.koin.feature.store.orders.component.SearchBar

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
    onDetailClick: (Int) -> Unit = { },
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
                        active = !filters.period.isDefault,
                        onClick = { openFilterOverlay() }
                    )
                }
                item {
                    KoinOrdersFilterChip(
                        text = stringResource(R.string.bullet_separator, stringResource(filters.type.stringRes), stringResource(filters.status.stringRes)),
                        active = !filters.type.isDefault or !filters.status.isDefault,
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
                            onDetailClick = { onDetailClick(orderdata.id) },
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
            orderStatus = OrderHistoryStatus.CANCELLED,
            orderDate = "9월 5일 (금)",
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
            orderDate = "9월 5일 (금)",
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
            orderStatus = OrderHistoryStatus.CANCELLED,
            orderDate = "9월 5일 (금)",
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
            orderDate = "9월 5일 (금)",
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
    val orderHistories: List<OrderHistoryData> = listOf()

    OrderHistoryScreen(
        OrderFilter(
            period = PeriodOption.NONE,
            type = TypeOption.NONE,
            status = StatusOption.NONE
        ),
        orderHistories = orderHistories
    )
}
