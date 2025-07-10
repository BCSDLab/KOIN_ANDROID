package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.store.ShopSearchRelated

@Composable
fun SearchResultItem(
    item: ShopSearchRelated,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (item.shopId == null) R.drawable.ic_search_related_menu else R.drawable.ic_store_search),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 24.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = item.keyword,
            modifier = Modifier.weight(1f),
            style = KoinTheme.typography.regular14.copy(color = KoinTheme.colors.neutral500)
        )

        if (item.shopId != null) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_up_left),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(24.dp)
            )
        }
    }
}
