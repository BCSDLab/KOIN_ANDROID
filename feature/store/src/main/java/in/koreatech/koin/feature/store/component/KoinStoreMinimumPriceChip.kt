package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinStoreMinimumPriceChip(
    minimumPrice: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    KoinStoreChip(
        modifier = modifier,
        text = if (minimumPrice == Int.MAX_VALUE) {
            stringResource(R.string.store_min_price_filter_text)
        } else {
            stringResource(R.string.store_min_price_filter_text_with_price, minimumPrice)
        },
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            elevation = if (minimumPrice != Int.MAX_VALUE) 0.dp else 4.dp,
            containerColor = if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0,
            textColor = if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400
        ),
        trailingIcon = painterResource(R.drawable.ic_store_arrow_down),
        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
            iconColor = if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400
        ),
        onClick = onClick
    )
}

@Preview
@Composable
private fun KoinStoreMinimumPriceChipPreview() {
    RebrandKoinTheme {
        KoinStoreMinimumPriceChip(
            minimumPrice = 0,
            modifier = Modifier.padding(16.dp)
        )
    }
}
