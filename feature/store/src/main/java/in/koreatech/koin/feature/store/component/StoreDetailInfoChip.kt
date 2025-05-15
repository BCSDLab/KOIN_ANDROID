package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.feature.store.R


@Composable
fun OriginInfoChips(){
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0)),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier
            .defaultMinSize(minHeight = 28.dp)
            .clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.store_info_and_origin),
                fontSize = 11.sp,
                color = Color(0xFF333333)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_store_info_origin_arrow),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
            )
        }
    }
}

@Composable
fun AvailableChips(storeInfo: StoreWithMenu){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (storeInfo.isCardOk) {
            AvailableChip(
                text = stringResource(R.string.card_available),
            )
        }
        if (storeInfo.isDeliveryOk) {
            AvailableChip(
                text = stringResource(R.string.delivery_available),
            )
        }
        if (storeInfo.isBankOk) {
            AvailableChip(
                text = stringResource(R.string.account_transfer_avilable),
            )
        }
    }
}


@Composable
fun AvailableChip(
    text: String,
) {
    Surface(
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(50),
        color = KoinTheme.colors.neutral0,
        modifier = Modifier
            .heightIn(min = 24.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = Color(0xFFBC7DFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
