package `in`.koreatech.koin.feature.store.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.StoreDetailInfoType
import `in`.koreatech.koin.feature.store.model.ShopInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel

@Composable
fun StoreDetailInfo(
    storeInfo: ShopInfoModel,
    storeReview: StoreReview,
    storeDescriptionModel: StoreDescriptionModel,
    modifier: Modifier = Modifier,
    navigateToReview: () -> Unit = {},
    navigateToDetailInfo: (selectedInfo: String) -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Text(modifier = Modifier.padding(vertical = 4.dp), text = storeInfo.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.clickable { navigateToReview() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null,
                    tint = colorResource(id = R.color.star)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = storeReview.statistics.averageRating.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = stringResource(R.string.review_count, storeReview.totalCount), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Icon(painter = painterResource(R.drawable.ic_right_arrow), contentDescription = null, modifier = Modifier.size(9.dp))
            }
            OriginInfoChips(
                modifier = Modifier
                    .defaultMinSize(minHeight = 28.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable {
                        navigateToDetailInfo(StoreDetailInfoType.ORIGIN.name)
                    }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AvailableChips(storeInfo)
        Spacer(modifier = Modifier.height(16.dp))
        StoreDetailInfoCard(
            storeInfo = storeInfo,
            modifier = Modifier.fillMaxWidth(),
            storeDescriptionModel = storeDescriptionModel,
            navigateToDetailInfo = { type -> navigateToDetailInfo(type) }
        )
    }
}

@Preview
@Composable
fun StoreDetailInfoPreview() {
    StoreDetailInfo(
        storeInfo = ShopInfoModel.empty(),
        storeReview = StoreReview.empty(),
        storeDescriptionModel = StoreDescriptionModel.empty()
    )
}
