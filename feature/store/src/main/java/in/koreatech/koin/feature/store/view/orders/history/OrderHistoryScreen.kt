package `in`.koreatech.koin.feature.store.view.orders.history

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinOrdersFilterChip
import `in`.koreatech.koin.feature.store.component.KoinOrdersResetChip
import `in`.koreatech.koin.feature.store.component.OrderHistoryCard
import `in`.koreatech.koin.feature.store.component.SearchBar
import `in`.koreatech.koin.feature.store.component.SearchBarFake
import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.OrderStatus
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData

@Composable
fun OrderHistoryScreen(
    filters: OrderFilter,
    orderHistories: List<OrderHistoryData>,
    modifier: Modifier = Modifier,
    isTyping: Boolean = false,
    searchQuery: String = "",
    onSearchStart: () -> Unit = { },
    onSearchCancel: () -> Unit = { },
    onQueryChanged: (String) -> Unit = { },
    resetFilter: () -> Unit = { },
    openFilterOverlay: () -> Unit = { },
    onDetailClick: (Int) -> Unit = { },
    onWriteReviewClick: (Int) -> Unit = { },
    onReorderClick: (Int) -> Unit = { }
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isTyping) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onQueryChanged,
                    modifier = Modifier.weight(1f),
                    hint = stringResource(R.string.order_history_searchbar_hint)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    modifier = Modifier.clickable { onSearchCancel() },
                    text = stringResource(R.string.cancel),
                    style = RebrandKoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.neutral500
                )
            } else {
                SearchBarFake(
                    query = searchQuery,
                    modifier = Modifier.weight(1f),
                    hint = stringResource(R.string.order_history_searchbar_hint),
                    onClick = onSearchStart
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!filters.isAllDefault()) {
                item {
                    KoinOrdersResetChip(
                        onClick = { resetFilter() }
                    )
                }
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.location.stringRes),
                    active = filters.location.isActivated,
                    onClick = { openFilterOverlay() }
                )
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.period.stringRes),
                    active = filters.period.isActivated,
                    onClick = { openFilterOverlay() }
                )
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.type.stringRes) + stringResource(R.string.bullet_separator) + stringResource(filters.status.stringRes),
                    active = filters.type.isActivated or filters.status.isActivated,
                    onClick = { openFilterOverlay() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
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

            if (isTyping) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.7f))
                )
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
            orderStatus = OrderStatus.CANCELLED,
            orderDate = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        ),
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderStatus.DELIVERED,
            orderDate = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )

    OrderHistoryScreen(
        OrderFilter(
            location = LocationOption.DEFAULT,
            period = PeriodOption.DEFAULT,
            type = TypeOption.DEFAULT,
            status = StatusOption.DEFAULT
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
            orderStatus = OrderStatus.CANCELLED,
            orderDate = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        ),
        OrderHistoryData(
            id = 1,
            paymentId = 1,
            orderableShopId = 1,
            orderStatus = OrderStatus.DELIVERED,
            orderDate = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            orderableShopName = "맛있는 족발 - 병천점",
            orderTitle = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )

    OrderHistoryScreen(
        OrderFilter(
            location = LocationOption.DEFAULT,
            period = PeriodOption.DEFAULT,
            type = TypeOption.DEFAULT,
            status = StatusOption.DEFAULT
        ),
        orderHistories = orderHistories,
        isTyping = true
    )
}
