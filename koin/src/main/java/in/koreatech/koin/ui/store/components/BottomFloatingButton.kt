package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun BottomFloatingButton(
    text: String,
    price: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.primary500, RebrandKoinTheme.shapes.medium)
            .clickable { onClick() }
            .padding(horizontal = 81.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = RebrandKoinTheme.colors.neutral0,
                style = RebrandKoinTheme.typography.medium14,
            )
            Text(
                text = stringResource(R.string.menu_detail_option_price, price),
                color = RebrandKoinTheme.colors.neutral0,
                style = RebrandKoinTheme.typography.bold18,
            )
        }
    }
}

@Composable
fun BottomFloatingLayout(
    text: String,
    price: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .border(width = 0.5.dp, color = RebrandKoinTheme.colors.neutral300, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .fillMaxWidth()
            .background(color = RebrandKoinTheme.colors.neutral0, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(horizontal = 32.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        BottomFloatingButton(
            text = text,
            price = price,
            onClick = onClick
        )
    }
}

@Preview
@Composable
fun BottomFloatingButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        BottomFloatingButton(
            text = "장바구니 추가",
            price = 1000,
            onClick = {}
        )

        BottomFloatingLayout(
            text = "장바구니 추가",
            price = 1000,
            onClick = {}
        )
    }
}
