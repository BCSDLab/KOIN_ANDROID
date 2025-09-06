package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderOnGoingData

@Composable
fun OrderOnGoingCard(
    orderdata: OrderOnGoingData,
    modifier: Modifier = Modifier
) {
    val (chipIcon, timeGuide) = when (orderdata.status) {
        TypeOption.TAKEOUT -> R.drawable.ic_order_takeout to R.string.takeout_time_guide
        else -> R.drawable.ic_order_delivery to R.string.delivery_time_guide
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(RebrandKoinTheme.colors.neutral0),
        border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(RebrandKoinTheme.colors.primary100)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = chipIcon),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.primary500,
                    modifier = modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(orderdata.status.stringRes),
                    style = RebrandKoinTheme.typography.medium12,
                    color = RebrandKoinTheme.colors.primary500
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = orderdata.time + stringResource(timeGuide),
                style = RebrandKoinTheme.typography.bold20,
                color = RebrandKoinTheme.colors.primary700
            )

            Text(
                text = stringResource(R.string.food_condition_guide),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = RebrandKoinTheme.colors.neutral200
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = orderdata.storeImageUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(11.dp))
                Column {
                    Text(
                        text = orderdata.storeName,
                        style = RebrandKoinTheme.typography.bold16,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = orderdata.orders,
                        style = RebrandKoinTheme.typography.medium14,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.order_histroy_price_won, orderdata.price),
                        style = RebrandKoinTheme.typography.bold14,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
                border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500)
            ) {
                Text(
                    text = stringResource(R.string.view_order_details),
                    style = RebrandKoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.primary500
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderOnGoingCardPreview() {
    OrderOnGoingCard(
        orderdata = OrderOnGoingData(
            TypeOption.DELIVERY,
            time = "오후 8:32",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderOnGoingCardPreview2() {
    OrderOnGoingCard(
        orderdata = OrderOnGoingData(
            TypeOption.TAKEOUT,
            time = "오후 8:32",
            storeImageUrl = "https://example.com/store_thumbnail.jpg",
            storeName = "맛있는 족발 - 병천점",
            orders = "족발 + 막국수 저녁 set 외 1건",
            price = 32500
        )
    )
}
