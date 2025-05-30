package `in`.koreatech.koin.feature.store.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.CartMenuItem
import `in`.koreatech.koin.feature.store.component.PaymentSummaryCard

@Composable
fun ShoppingCartContent(
    modifier: Modifier,
    storeInfo: StoreDetailInfo
) {
    val tabs = listOf(R.string.delivery, R.string.pickup)
    var selectedTab by remember { mutableStateOf(R.string.delivery) }
    val lazyColumnState = rememberLazyListState()
    LazyColumn(
        state = lazyColumnState,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.store_detail_background)
            )
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(55.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.05f),
                            topLeft = Offset(0f, 4.dp.toPx()),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    }
                    .padding(2.dp)
                    .background(KoinTheme.colors.neutral0, shape = RoundedCornerShape(10.dp))
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedTab
                    Button(
                        onClick = { selectedTab = tab },
                        modifier = Modifier
                            .heightIn(52.dp)
                            .weight(1f)
                            .padding(4.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = buttonColors(
                            containerColor = if (isSelected) colorResource(R.color.shopping_cart_button_background) else KoinTheme.colors.neutral0,
                            contentColor = if (isSelected) KoinTheme.colors.neutral0 else KoinTheme.colors.neutral500
                        )
                    ) {
                        Text(
                            style = KoinTheme.typography.medium16,
                            text = stringResource(id = tab)
                        )
                    }
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: navigate */ }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(storeInfo.imageUrls.firstOrNull()),
                    contentDescription = "",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = storeInfo.name,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = cardElevation(0.5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = KoinTheme.colors.neutral0
                )
            ) {
                Column {
                    repeat(3) { // 장바구니에 담긴 메뉴 수
                        CartMenuItem(
                            menu = ShopMenus(
                                name = "족발 + 막국 저녁 set",
                                description = "가격 : 25000원\n사이즈 : 소\n음료:콜라 500ml",
                                imageUrls = listOf("https://example.com/image.jpg"),
                                optionPrices = listOf(
                                    ShopMenus.ShopMenuOptions("옵션2", 2000)
                                ),
                                isSingle = true,
                                singlePrice = 10000,
                                isHidden = false,
                                id = 1
                            )
                        )
                        if (it != 2) {
                            Divider(
                                color = KoinTheme.colors.neutral300,
                                thickness = 2.dp
                            )
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.05f),
                            topLeft = Offset(0f, 4.dp.toPx()),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    },
                shape = KoinTheme.shapes.extraSmall,
                colors = buttonColors(
                    containerColor = KoinTheme.colors.neutral0,
                    contentColor = colorResource(R.color.shopping_cart_button_text)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(R.string.plus), fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.add_more), style= KoinTheme.typography.bold18)
                }
            }
        }
        item { PaymentSummaryCard() }
    }
}

@Composable
fun ShoppingCartEmptyContent(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = colorResource(R.color.store_detail_background)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_cart),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.shopping_cart_is_empty)
        )
        Button(
            onClick = {},
            modifier = Modifier
                .padding(top = 20.dp),
            colors = buttonColors(
                containerColor = KoinTheme.colors.neutral0,
                contentColor = KoinTheme.colors.neutral500
            ),
            shape = KoinTheme.shapes.extraSmall,
            elevation = buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp,
                focusedElevation = 2.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_menu),
                style = KoinTheme.typography.medium16
            )
        }
    }
}

@Composable
@Preview
private fun ShoppingCartContentPreview() {
    KoinTheme {
        ShoppingCartEmptyContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
