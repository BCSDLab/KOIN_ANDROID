package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.ShopMenus

@Composable
fun CartMenuItem(
    menu: ShopMenus
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = menu.name, fontWeight = SemiBold, fontSize = 18.sp)
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = menu.description ?: "",
                    style = KoinTheme.typography.medium15,
                    color = KoinTheme.colors.neutral500
                )
                Text(
                    text = "27,000원",
                    style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.neutral800
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(menu.imageUrls?.firstOrNull())
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
            quantity = 1,
            onOptionClick = {},
            onMinusClick = {},
            onPlusClick = {}
        )
    }
}

@Composable
@Preview
private fun ShoppingCartItem() {
    KoinTheme {
        CartMenuItem(
            menu = ShopMenus(
                id = 1,
                name = "맛있는 메뉴",
                description = "이 메뉴는 정말 맛있습니다.",
                imageUrls = listOf("https://example.com/image.jpg"),
                isHidden = false,
                isSingle = false,
                optionPrices = listOf(
                    ShopMenus.ShopMenuOptions(option = "123123", price = 123213),
                    ShopMenus.ShopMenuOptions(option = "3232", price = 321312213)
                ),
                singlePrice = 10000
            )
        )
    }
}
