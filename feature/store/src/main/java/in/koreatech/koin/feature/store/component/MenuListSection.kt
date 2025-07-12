package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.MenuModel

fun LazyListScope.menuListSection(
    category: String,
    menus: List<MenuModel>
) {
    if (menus.isEmpty()) return

    item {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
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
    menu: MenuModel
) {
    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = menu.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = menu.description ?: "",
                fontSize = 12.sp,
                color = KoinTheme.colors.neutral500
            )
            OptionPriceText(
                shopMenus = menu
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(menu.thumbnailImage)
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

@Composable
private fun OptionPriceText(
    shopMenus: MenuModel
) {
    Column(
        modifier = Modifier.padding(top = 4.dp)
    ) {
        if (shopMenus.isSingle) {
            Text(
                text = stringResource(R.string.price_with_won, shopMenus.singlePrice ?: 0),
                style = KoinTheme.typography.bold14
            )
        } else {
            val options = shopMenus.prices.fold("") { acc, menu ->
                acc + stringResource(R.string.option_price, menu.name ?: "", menu.price ?: 0)
            }.trim()
            Text(text = options, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                category = "추천메뉴",
                menus = listOf(
                    MenuModel(
                        id = 1,
                        name = "아메리카노",
                        description = "진한 커피의 맛을 느껴보세요.",
                        thumbnailImage = "https://example.com/americano.jpg",
                        isSoldOut = false,
                        prices = listOf(
                        ),
                        isSingle = true
                    ),
                    MenuModel(
                        id = 2,
                        name = "카페라떼",
                        description = "부드러운 ��유와 커피의 조화.",
                        thumbnailImage = "https://example.com/cafelatte.jpg",
                        isSoldOut = false,
                        prices = listOf(
                        ),
                        isSingle = true
                    )
                )
            )
        }
    }
}
