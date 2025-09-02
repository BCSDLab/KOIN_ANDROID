package `in`.koreatech.koin.feature.store.cart.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.Cart.CartItem
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.QuantityOptionButton

@Composable
fun CartMenuItem(
    menu: CartItem,
    modifier: Modifier = Modifier,
    navigateToMenu: (Int) -> Unit = { },
    onChangeQuantity: (Int, Int) -> Unit = { _, _ -> },
    onDeleteMenuItem: (Int) -> Unit = { }
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(modifier = Modifier.padding(vertical = 8.dp), text = menu.name, fontWeight = SemiBold, fontSize = 18.sp)
                Text(
                    text = stringResource(R.string.store_cart_price, menu.totalAmount),
                    style = KoinTheme.typography.medium15,
                    color = KoinTheme.colors.neutral500
                )

                if (menu.options.isNotEmpty()) {
                    Text(
                        text = menu.options.joinToString("\n") { context.getString(R.string.store_cart_option_price, it.optionName, it.optionPrice) },
                        style = KoinTheme.typography.medium15,
                        color = KoinTheme.colors.neutral500
                    )
                }

                Text(
                    text = stringResource(R.string.price_with_won, menu.totalAmount),
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(menu.menuThumbnailImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(88.dp)
                    .clip(KoinTheme.shapes.small)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        QuantityOptionButton(
            modifier = Modifier.fillMaxWidth(),
            quantity = menu.quantity,
            onOptionClick = {
                navigateToMenu(menu.cartMenuItemId)
            },
            onMinusClick = {
                onChangeQuantity(menu.cartMenuItemId, menu.quantity - 1)
            },
            onDeleteMenuClick = {
                onDeleteMenuItem(menu.cartMenuItemId)
            },
            onPlusClick = {
                onChangeQuantity(menu.cartMenuItemId, menu.quantity + 1)
            }
        )
    }
}

@Composable
@Preview
private fun ShoppingCartItem() {
    KoinTheme {
        CartMenuItem(
            menu = CartItem(
                cartMenuItemId = 1,
                name = "아메리카노",
                totalAmount = 4500,
                quantity = 2,
                menuThumbnailImageUrl = "https://example.com/image.jpg",
                options = listOf(
                    CartItem.CartOption(optionGroupName = "테스트", optionName = "샷 추가", optionPrice = 1500)
                ),
                price = CartItem.CartPrice(
                    name = "아메리카노",
                    price = 4500
                ),
                isModified = false,
                orderableShopMenuId = 0
            )
        )
    }
}
