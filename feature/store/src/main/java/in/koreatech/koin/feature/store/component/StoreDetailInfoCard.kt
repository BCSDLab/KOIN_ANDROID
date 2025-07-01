package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.feature.store.R

@Composable
fun StoreDetailInfoCard(
    storeInfo: StoreWithMenu,
    navigateToDetailInfo: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DeliveryInfoCard(
            modifier = Modifier.weight(1f),
            storeInfo,
            navigateToDetailInfo = navigateToDetailInfo
        )
        Spacer(modifier = Modifier.width(8.dp))
        NoticeCard(
            modifier = Modifier.weight(1f),
            description = storeInfo.description,
            navigateToDetailInfo = navigateToDetailInfo
        )
    }
}

@Composable
fun DeliveryInfoCard(
    modifier: Modifier = Modifier,
    storeInfo: StoreWithMenu,
    navigateToDetailInfo: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .widthIn(175.dp)
            .heightIn(60.dp)
            .clickable { navigateToDetailInfo() },
        shadowElevation = 1.dp,
        color = KoinTheme.colors.neutral0
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row {
                    Text(text = stringResource(R.string.minimum_order), fontSize = 12.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "TODO: 최소주문", fontSize = 12.sp, lineHeight = 18.sp, color = KoinTheme.colors.neutral500)
                }
                Row {
                    Text(text = stringResource(R.string.delivery_fee), fontSize = 12.sp, lineHeight = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(storeInfo.deliveryPrice.toString() + "원", fontSize = 12.sp, lineHeight = 18.sp, color = KoinTheme.colors.neutral500)
                }
            }
            Icon(painter = painterResource(id = R.drawable.ic_delivery_arrow_right), contentDescription = null, modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun NoticeCard(
    modifier: Modifier,
    description: String?,
    navigateToDetailInfo: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .widthIn(175.dp)
            .heightIn(60.dp)
            .clickable { navigateToDetailInfo() },
        shadowElevation = 1.dp,
        color = KoinTheme.colors.neutral0
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(ImageVector.vectorResource(id = R.drawable.ic_store_notice), contentDescription = null, modifier = Modifier.size(16.dp), tint = colorResource(id = R.color.store_detail_chip))
            Spacer(modifier = Modifier.width(7.dp))
            Text(modifier = Modifier.width(97.dp), overflow = TextOverflow.Ellipsis, text = description ?: "", fontSize = 12.sp, lineHeight = 18.sp, maxLines = 2)
            Spacer(modifier = Modifier.width(7.dp))
            Icon(painter = painterResource(id = R.drawable.ic_delivery_arrow_right), contentDescription = null, modifier = Modifier.size(10.dp))
        }
    }
}
