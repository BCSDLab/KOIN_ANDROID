package `in`.koreatech.koin.feature.store.view.orders.history

import androidx.compose.foundation.background
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
import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.OrderStatus
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter
import `in`.koreatech.koin.feature.store.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.model.filters

@Composable
fun OrderHistoryScreen(
    filters: OrderFilter,
    orderHistorys: List<OrderHistoryData>,
    modifier: Modifier = Modifier,
    isTyping: Boolean = false,
    openFilterDialog: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = "",
                onQueryChange = {},
                modifier = Modifier.weight(1f),
                hint = stringResource(R.string.order_history_searchbar_hint)
            )

            if (isTyping) {
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.cancel),
                    style = RebrandKoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                KoinOrdersResetChip()
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.location.stringRes),
                    active = filters.location.isActivated,
                    onClick = { openFilterDialog() }
                )
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.period.stringRes),
                    active = filters.period.isActivated,
                    onClick = { openFilterDialog() }
                )
            }
            item {
                KoinOrdersFilterChip(
                    text = stringResource(filters.type.stringRes) + stringResource(R.string.bullet_separator) + stringResource(filters.status.stringRes),
                    active = filters.type.isActivated and filters.status.isActivated,
                    onClick = { openFilterDialog() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orderHistorys) { orderdata ->
                    OrderHistoryCard(
                        orderdata = orderdata,
                        onDetailClick = { },
                        onWriteReviewClick = { },
                        onReorderClick = { }
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
private fun OrderHistoryPreview() {
    val orderHistorys: List<OrderHistoryData> = listOf(
        OrderHistoryData(
            OrderStatus.CANCELLED,
            date = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.OPEN,
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        ),
        OrderHistoryData(
            OrderStatus.DELIVERED,
            date = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )

    OrderHistoryScreen(
        filters = filters,
        orderHistorys = orderHistorys
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryPreview2() {
    val orderHistorys: List<OrderHistoryData> = listOf(
        OrderHistoryData(
            OrderStatus.CANCELLED,
            date = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.OPEN,
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        ),
        OrderHistoryData(
            OrderStatus.DELIVERED,
            date = "9월 5일 (금)",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeStatus = StoreStatus.SOLD_OUT,
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )

    OrderHistoryScreen(
        filters = OrderFilter(
            location = LocationOption.DEFAULT,
            period = PeriodOption.DEFAULT,
            type = TypeOption.DEFAULT,
            status = StatusOption.DEFAULT
        ),
        orderHistorys = orderHistorys,
        isTyping = true
    )
}
