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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.CartMenuItem
import `in`.koreatech.koin.feature.store.component.DeleteCartDialog
import `in`.koreatech.koin.feature.store.component.PaymentSummaryCard

@Composable
fun ShoppingCartContent(
    cart: Cart,
    modifier: Modifier = Modifier,
    cartType: CartType = CartType.DELIVERY,
    isOperating: Boolean = true,
    dialogVisibility: Boolean = false,
    navigateToStoreDetail: (Int) -> Unit = { },
    navigateToCartEdit: (Int) -> Unit = { },
    onOrderModeChanged: (CartType) -> Unit = { },
    onChangeQuantity: (Int, Int) -> Unit = { _, _ -> },
    onResetMenu: () -> Unit = { },
    deleteCartMenuItem: (Int) -> Unit = { },
    setDialogVisibility: (Boolean) -> Unit = { }
) {
    val lazyColumnState = rememberLazyListState()

    if (dialogVisibility) {
        DeleteCartDialog(
            onConfirm = {
                onResetMenu()
            },
            onDismiss = {
                setDialogVisibility(false)
            },
            dialogMessage = if (!isOperating) stringResource(R.string.store_is_not_operating_dialog_message) else stringResource(R.string.delete_menu_dialog_message)
        )
    }
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
                    .background(RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.medium)
            ) {
                Button(
                    modifier = Modifier
                        .heightIn(52.dp)
                        .weight(1f)
                        .padding(4.dp),
                    onClick = {
                        if (cartType != CartType.DELIVERY) {
                            onOrderModeChanged(
                                CartType.DELIVERY
                            )
                        }
                    },
                    enabled = cart.isDeliveryAvailable,
                    shape = RebrandKoinTheme.shapes.medium,
                    colors = buttonColors(
                        containerColor = if (cartType == CartType.DELIVERY) colorResource(R.color.shopping_cart_button_background) else KoinTheme.colors.neutral0,
                        contentColor = if (cartType == CartType.DELIVERY) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral0
                    )
                ) {
                    Text(
                        style = KoinTheme.typography.medium16,
                        text = stringResource(id = R.string.delivery)
                    )
                }
                Button(
                    modifier = Modifier
                        .heightIn(52.dp)
                        .weight(1f)
                        .padding(4.dp),
                    onClick = {
                        if (cartType != CartType.TAKE_OUT) {
                            onOrderModeChanged(
                                CartType.TAKE_OUT
                            )
                        }
                    },
                    enabled = cart.isTakeoutAvailable,
                    shape = RebrandKoinTheme.shapes.medium,
                    colors = buttonColors(
                        containerColor = if (cartType == CartType.TAKE_OUT) colorResource(R.color.shopping_cart_button_background) else RebrandKoinTheme.colors.neutral0,
                        contentColor = if (cartType == CartType.TAKE_OUT) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral0
                    )
                ) {
                    Text(
                        style = RebrandKoinTheme.typography.medium16,
                        text = stringResource(id = R.string.pickup)
                    )
                }
            }
            if (!(cart.isDeliveryAvailable && cart.isTakeoutAvailable)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500
                    )
                    Text(
                        text = stringResource(
                            R.string.order_only_notice,
                            if (cart.isDeliveryAvailable) stringResource(R.string.delivery) else stringResource(R.string.pickup)
                        ),
                        style = RebrandKoinTheme.typography.regular13,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 4.dp)
                    .clickable {
                        cart.orderableShopId?.let { storeId ->
                            navigateToStoreDetail(storeId)
                        }
                    }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(cart.shopThumbnailImageUrl),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = cart.shopName ?: "",
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
                shape = RebrandKoinTheme.shapes.medium,
                elevation = cardElevation(0.5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = KoinTheme.colors.neutral0
                )
            ) {
                Column {
                    cart.items.forEachIndexed { index, cartItem ->
                        CartMenuItem(
                            modifier = Modifier
                                .padding(16.dp),
                            menu = cartItem,
                            navigateToMenu = navigateToCartEdit,
                            onChangeQuantity = { menuId, quantity ->
                                onChangeQuantity(menuId, quantity)
                            },
                            onDeleteMenuItem = { menuId ->
                                deleteCartMenuItem(menuId)
                            }
                        )
                        if (index != cart.items.size - 1) {
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
                onClick = {
                    cart.orderableShopId?.let { storeId ->
                        navigateToStoreDetail(storeId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.05f),
                            topLeft = Offset(0f, 4.dp.toPx()),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    },
                shape = KoinTheme.shapes.small,
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
                    Text(text = stringResource(R.string.add_more), style = KoinTheme.typography.bold18)
                }
            }
        }
        item {
            PaymentSummaryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                itemAmount = cart.itemsAmount,
                deliveryFee = cart.deliveryFee,
                totalAmount = cart.totalAmount,
                finalPaymentAmount = cart.finalPaymentAmount
            )
        }
    }
}

@Composable
fun ShoppingCartEmptyContent(
    modifier: Modifier = Modifier,
    navigateToStoreMain: () -> Unit = { }
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.shopping_cart_is_empty),
            style = KoinTheme.typography.bold16
        )
        Button(
            onClick = {
                navigateToStoreMain()
            },
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
        /*   ShoppingCartContent(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(16.dp),
               cart = Cart.Empty,
           )*/
    }
}
