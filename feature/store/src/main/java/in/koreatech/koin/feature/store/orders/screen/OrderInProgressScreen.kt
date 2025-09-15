package `in`.koreatech.koin.feature.store.orders.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.OrderInProgressStatus
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderInProgressData
import `in`.koreatech.koin.feature.store.orders.component.OrderInProgressCard
import java.time.LocalTime

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
