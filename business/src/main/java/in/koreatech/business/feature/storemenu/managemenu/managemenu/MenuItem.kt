package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorCategory
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories

@Composable
fun MenuItem(
    menuList: StoreMenuCategories,
    onModifyMenuClicked: (Int) -> Unit = {},
    onDeleteMenuClicked:(ShopMenus) -> Unit = {},
) {
    menuList.menus?.forEach { item ->
        MenuItemFactor(
            item = item,
            onModifyMenuClicked = onModifyMenuClicked,
            onDeleteMenuClicked = onDeleteMenuClicked
        )
    }
}

@Composable
fun MenuCategories(item: StoreMenuCategories) {
    Row(
        modifier =
            Modifier
                .padding(horizontal = 25.dp)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter =
                painterResource(
                    id =
                        when (item.id) {
                            1 -> R.drawable.ic_recommend
                            2 -> R.drawable.ic_main
                            3 -> R.drawable.ic_set
                            4 -> R.drawable.ic_side
                            else -> R.drawable.ic_recommend
                        },
                ),
            contentDescription = stringResource(R.string.category),
        )
        Text(
            text = item.name ?: "",
            modifier = Modifier.padding(10.dp),
            style = KoinTheme.typography.medium18,
            color = ColorCategory,
            fontWeight = FontWeight(500),
        )
    }
}

@Composable
fun MenuItemFactor(
    modifier: Modifier = Modifier,
    onModifyMenuClicked: (Int) -> Unit = {},
    onDeleteMenuClicked:(ShopMenus) -> Unit = {},
    item: ShopMenus
){
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 10.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = item.name,
                        style = KoinTheme.typography.medium16,
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 5.dp)
                            .border(1.dp, ColorPrimary, RoundedCornerShape(8.dp))
                            .clickable {
                                onModifyMenuClicked(item.id)
                            }
                    ) {
                        Text(
                            modifier =
                                Modifier
                                    .padding(horizontal = 9.dp, vertical = 5.dp),
                            text = stringResource(R.string.common_change),
                            style = KoinTheme.typography.regular10,
                            color = ColorPrimary,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .border(1.dp, KoinTheme.colors.danger500, RoundedCornerShape(8.dp))
                            .clickable {
                                onDeleteMenuClicked(item)
                            }
                    ) {
                        Text(
                            modifier =
                                Modifier
                                    .padding(horizontal = 9.dp, vertical = 5.dp),
                            text = stringResource(R.string.common_delete),
                            style = KoinTheme.typography.regular10,
                            color = KoinTheme.colors.danger500,
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = item.singlePrice.toString() + "원",
                    color = ColorPrimary,
                )
            }
            Image(
                modifier =
                    Modifier
                        .width(68.dp)
                        .height(68.dp),
                contentScale = ContentScale.Crop,
                painter =
                    rememberAsyncImagePainter(
                        model = item.imageUrls?.firstOrNull() ?: R.drawable.ic_koin_logo,
                    ),
                contentDescription = stringResource(R.string.menu_default_image),
            )
        }
        Divider(
            color = ColorTextField,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(1.dp),
        )
    }
}

@Preview
@Composable
fun PreviewMenuItemFactor(modifier: Modifier = Modifier) {
    val item =
        ShopMenus(
            id = 2795,
            name = "qweqwe",
            isHidden = false,
            isSingle = false,
            singlePrice = null,
            optionPrices =
                listOf(
                    ShopMenus.ShopMenuOptions(option = "123123", price = 123213),
                    ShopMenus.ShopMenuOptions(option = "3232", price = 321312213),
                ),
            description = "saadafgsdrtddffewrsfdsfwerewrs",
            imageUrls =
                listOf(
                    "https://stage-static.koreatech.in/upload/MARKET/2024/8/21/1fb3219e-dc58-4f73-85f0-8ae9f1c1eea7/1000000043.jpg",
                    "https://stage-static.koreatech.in/upload/MARKET/2024/8/21/5e95c4a1-bca6-4d9e-bf8c-2b3f3929b7d0/1000000039.jpg",
                ),
        )

    MenuItemFactor(
        item = item,
    )
}
