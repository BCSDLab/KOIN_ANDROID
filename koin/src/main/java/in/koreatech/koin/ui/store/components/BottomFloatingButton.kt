package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun BottomFloatingButton(
    text: String,
    price: Int,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.primary500, RoundedCornerShape(12.dp))
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
                style = KoinTheme.typography.medium14,
                fontSize = 14.sp
            )
            Text(
                text = "${price}원",
                color = RebrandKoinTheme.colors.neutral0,
                style = RebrandKoinTheme.typography.bold18,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun BottomFloatingLayout(
    text: String,
    price: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(width = 0.5.dp, color = RebrandKoinTheme.colors.neutral300, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .fillMaxWidth()
            .background(color = RebrandKoinTheme.colors.neutral0, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(horizontal = 32.dp, vertical = 12.dp)
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
