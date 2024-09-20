package `in`.koreatech.business.feature.storemenu

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray7
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice

@Composable
fun LazyItemScope.TitleAndContent(
    stringId: Int,
    content: String
){
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        text = stringResource(id = stringId),
        fontSize = 15.sp,
        color = ColorPrimary,
        fontWeight = FontWeight.Bold
    )
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp),
        text = content,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
    )
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        thickness = 1.dp,
        color = Gray7
    )
}

@Composable
fun LazyItemScope.TitleAndOptionPrice(
    optionPriceList: List<StoreMenuOptionPrice>,
    menuPrice: String
){
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        text = stringResource(id = R.string.menu_price),
        fontSize = 15.sp,
        color = ColorPrimary,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ){
        if(optionPriceList.isEmpty()){
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.menu_price_won, menuPrice),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                optionPriceList.forEach { menuDetailPrice ->
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(id = R.string.menu_price_many_won, menuDetailPrice.option, menuDetailPrice.price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        thickness = 1.dp,
        color = Gray7
    )
}

@Composable
fun LazyItemScope.TitleAndImageString(
    imageStringList: List<String>,
){
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        text = stringResource(id = R.string.menu_image),
        fontSize = 15.sp,
        color = ColorPrimary,
        fontWeight = FontWeight.Bold
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        items(imageStringList) { item ->
            if (item != stringResource(id = R.string.temp_uri)){
                Image(
                    modifier = Modifier
                        .size(137.dp)
                        .padding(bottom = 16.dp)
                        .padding(end = 16.dp),
                    painter = rememberAsyncImagePainter(
                        item
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun LazyItemScope.TitleAndImageUri(
    imageUriList: List<Uri>,
){
    Text(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
        text = stringResource(id = R.string.menu_image),
        fontSize = 15.sp,
        color = ColorPrimary,
        fontWeight = FontWeight.Bold
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        items(imageUriList) { item ->
            if (item != Uri.EMPTY){
                Image(
                    modifier = Modifier
                        .size(137.dp)
                        .padding(bottom = 16.dp)
                        .padding(end = 16.dp)
                    ,
                    painter = rememberAsyncImagePainter(
                        item
                    ),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}