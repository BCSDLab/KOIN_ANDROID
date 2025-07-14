package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.feature.store.R

fun LazyListScope.menuListSection(
    category: String,
    menus: List<ShopMenus>,
    modifier: Modifier = Modifier
) {
    if (menus.isEmpty()) return

    item {
        Column(
            modifier = modifier
        ) {
            Text(
                text = category,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(0.5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = KoinTheme.colors.neutral0
                )
            ) {
                Column {
                    menus.forEachIndexed { index, menu ->
                        MenuItem(menu = menu)
                        if (index != menus.lastIndex) {
                            Divider(
                                color = KoinTheme.colors.neutral300,
                                thickness = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    menu: ShopMenus
) {
    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = menu.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            if (menu.description != null) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = menu.description ?: "",
                    fontSize = 12.sp,
                    color = KoinTheme.colors.neutral500
                )
            }
            OptionPriceText(
                modifier = Modifier.padding(top = 4.dp),
                shopMenus = menu
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        menu.imageUrls
            ?.firstOrNull()?.let { url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
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
    }
}

@Composable
private fun OptionPriceText(
    modifier: Modifier = Modifier,
    shopMenus: ShopMenus
) {
    Column(
        modifier = modifier
    ) {
        if (shopMenus.isSingle) {
            Text(
                text = stringResource(R.string.price_with_won, shopMenus.singlePrice ?: ""),
                style = KoinTheme.typography.bold14
            )
        } else if (shopMenus.optionPrices?.isNotEmpty() == true) {
            val options = shopMenus.optionPrices?.fold("") { acc, menu ->
                acc + stringResource(R.string.option_price, menu.option, menu.price ?: "")
            }?.trim()
            Text(text = options ?: "", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuListSectionPreview() {
    KoinTheme {
        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            menuListSection(
                modifier = Modifier.fillMaxWidth(),
                category = "추천메뉴",
                menus = listOf(
                    ShopMenus(
                        name = "막국수",
                        description = "막국수 + 족발",
                        isSingle = false,
                        singlePrice = 10000,
                        optionPrices =
                        listOf(
                            ShopMenus.ShopMenuOptions("소", 10000),
                            ShopMenus.ShopMenuOptions("중", 20000),
                            ShopMenus.ShopMenuOptions("대", 30000)
                        ),
                        isHidden = false,
                        imageUrls = listOf(
                            "https://example.com/image1.jpg"
                        ),
                        id = 1
                    )
                )
            )
        }
    }
}
