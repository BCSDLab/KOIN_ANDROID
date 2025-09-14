package `in`.koreatech.koin.feature.store.orders.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.OrderHistoryStatus
import `in`.koreatech.koin.feature.store.enums.StoreStatus
import `in`.koreatech.koin.feature.store.model.OrderHistoryData

@Composable
fun OrderHistoryCard(
    orderdata: OrderHistoryData,
    modifier: Modifier = Modifier,
    onDetailClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    onReorderClick: () -> Unit = {}
) {
    val textColor = if (orderdata.orderStatus.isActivated) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral400

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RebrandKoinTheme.shapes.medium,
        colors = CardDefaults.cardColors(RebrandKoinTheme.colors.neutral0),
        border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(orderdata.orderStatus.stringRes),
                    style = RebrandKoinTheme.typography.bold16,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = orderdata.orderDate,
                    style = RebrandKoinTheme.typography.regular12,
                    color = textColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable(onClick = onDetailClick)
                ) {
                    Text(
                        text = stringResource(R.string.order_detail),
                        style = RebrandKoinTheme.typography.medium12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right_round),
                        contentDescription = "",
                        tint = RebrandKoinTheme.colors.neutral500
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = RebrandKoinTheme.colors.neutral200
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = orderdata.orderableShopThumbnail,
                    contentDescription = "",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RebrandKoinTheme.shapes.extraSmall),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(11.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = orderdata.orderableShopName,
                        style = RebrandKoinTheme.typography.bold16,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = orderdata.orderTitle,
                        style = RebrandKoinTheme.typography.medium14,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.order_histroy_price_won, orderdata.totalAmount),
                        style = RebrandKoinTheme.typography.bold14,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (orderdata.orderStatus.isActivated) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onWriteReviewClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400)
                ) {
                    Text(
                        text = stringResource(R.string.write_review),
                        style = RebrandKoinTheme.typography.bold14,
                        color = RebrandKoinTheme.colors.neutral600
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
            }

            MenuAddButton(
                status = orderdata.openStatus,
                onClick = onReorderClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryCardPreview() {
    OrderHistoryCard(
        orderdata = OrderHistoryData(
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
        ),
        onDetailClick = { },
        onWriteReviewClick = { },
        onReorderClick = { }
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderHistoryCardPreview2() {
    OrderHistoryCard(
        orderdata = OrderHistoryData(
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
        ),
        onDetailClick = { },
        onWriteReviewClick = { },
        onReorderClick = { }
    )
}
