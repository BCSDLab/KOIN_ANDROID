package `in`.koreatech.business.feature.store.storedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.koin.domain.util.StoreUtil


fun LazyListScope.storeDetailInfo(
    infoDataList: List<Pair<String, String>>
) {
    infoDataList.forEach { (info, data) ->
        info(info, data)
    }
}

fun LazyListScope.info(info: String, data: String?) {
    item {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = info,
                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                )
                Text(
                    text = data ?: "",
                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                )
            }
        }
    }
}

@Composable
fun getInfoDataList(state: MyStoreDetailState): List<Pair<String, String>> {
    return listOf(
        Pair(stringResource(id = R.string.telephone_number), state.storeInfo?.phone ?: ""),
        Pair(
            stringResource(id = R.string.operating_time),
            state.storeInfo?.operatingTime?.joinToString(separator = "\n") {
                it.dayOfWeek + " " +
                StoreUtil.generateOpenCloseTimeString(
                    it.openTime,
                    it.closeTime
                )
            } ?: ""
        ),
        Pair(stringResource(id = R.string.address), state.storeInfo?.address ?: ""),
        Pair(
            stringResource(id = R.string.delivery_amount),
            "${state.storeInfo?.deliveryPrice ?: 0}원"
        ),
        Pair(stringResource(id = R.string.other_info), state.storeInfo?.description ?: "")
    )
}
