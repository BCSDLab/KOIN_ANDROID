package `in`.koreatech.business.feature.store.storedetail.menu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorCategory
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories



@Composable
fun MenuItem(
    menuList: StoreMenuCategories,
    onMenuClicked:(Int) -> Unit = {}
) {
    menuList.menus?.forEach { item ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 10.dp)
                .clickable {
                    onMenuClicked(item.id)
                }
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item.name, fontWeight = FontWeight(500))
                Text(text = item.singlePrice.toString() + "원", color = ColorPrimary)
            }
            Image(
                modifier = Modifier
                    .width(68.dp)
                    .height(68.dp),
                contentScale = ContentScale.Crop
                ,
                painter = if(item.imageUrls.isNullOrEmpty())painterResource(id = R.drawable.ic_koin_logo)else rememberAsyncImagePainter(
                    item.imageUrls!![0]
                ),
                contentDescription = stringResource(R.string.menu_default_image),
            )
        }
        Divider(
            color = ColorTextField,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(1.dp)
        )
    }
}

@Composable
fun MenuCategories(item: StoreMenuCategories) {
    Row(
        modifier = Modifier
            .padding(horizontal = 25.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(20.dp), painter = painterResource(
                id = when (item.id) {
                    1 -> R.drawable.ic_recommend
                    2 -> R.drawable.ic_main
                    3 -> R.drawable.ic_set
                    4 -> R.drawable.ic_side
                    else -> R.drawable.ic_recommend
                }
            ), contentDescription = stringResource(R.string.category)
        )
        Text(
            text = item.name ?: "",
            modifier = Modifier.padding(10.dp),
            color = ColorCategory,
            fontWeight = FontWeight(500),
            fontSize = 18.sp
        )
    }
}
