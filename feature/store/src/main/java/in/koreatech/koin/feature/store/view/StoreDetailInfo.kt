package `in`.koreatech.koin.feature.store.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.AvailableChips
import `in`.koreatech.koin.feature.store.component.OriginInfoChips
import `in`.koreatech.koin.feature.store.component.StoreDetailInfoCard

@Composable
fun StoreDetailInfo(
    storeInfo: StoreWithMenu,
    storeReview: StoreReview,
    navigateToReview: () -> Unit,
    navigateToDetailInfo: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp,)) {
        Text(modifier = Modifier.padding(vertical = 4.dp), text = storeInfo.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier= Modifier.clickable { navigateToReview() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
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
            OriginInfoChips()
        }
        Spacer(modifier = Modifier.height(12.dp))
        AvailableChips(storeInfo)
        Spacer(modifier = Modifier.height(16.dp))
        StoreDetailInfoCard(storeInfo, navigateToDetailInfo )
    }
}
