package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
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
    Row(
        modifier = modifier
            .then(
                if (minimumPrice != Int.MAX_VALUE) {
                    Modifier
                } else {
                    Modifier.shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = RebrandKoinTheme.colors.neutral400,
                        spotColor = RebrandKoinTheme.colors.neutral500
                    )
                }
            )
            .clip(RoundedCornerShape(24.dp))
            .background(if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0, shape = RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = if (minimumPrice == Int.MAX_VALUE) {
                stringResource(R.string.store_min_price_filter_text)
            } else {
                stringResource(R.string.store_min_price_filter_text_with_price, minimumPrice)
            },
            style = RebrandKoinTheme.typography.medium14.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    trim = LineHeightStyle.Trim.Both,
                    alignment = LineHeightStyle.Alignment.Center
                ),
                color = if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400
            )
        )

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .size(16.dp)
                .paint(
                    painter = painterResource(R.drawable.ic_store_arrow_down),
                    colorFilter = ColorFilter.tint(if (minimumPrice != Int.MAX_VALUE) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400)
                )
        )
    }
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
