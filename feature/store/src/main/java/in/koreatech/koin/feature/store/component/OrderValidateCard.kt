package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun OrderBottomBar(
    totalPrice: Int,
    isOrderEnabled: Boolean,
    orderableMessage: String,
    modifier: Modifier = Modifier,
    navigateToCart: () -> Unit = {}
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RebrandKoinTheme.colors.neutral0)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.price_with_won, totalPrice),
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    text = orderableMessage,
                    style = RebrandKoinTheme.typography.medium14,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }

            Button(
                modifier = Modifier
                    .widthIn(180.dp)
                    .heightIn(44.dp),
                onClick = navigateToCart,
                shape = RebrandKoinTheme.shapes.large,
                enabled = isOrderEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    contentColor = RebrandKoinTheme.colors.neutral0,
                    disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                    disabledContentColor = RebrandKoinTheme.colors.neutral0
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_coin),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = stringResource(R.string.order))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderBottomBarPreview() {
    KoinTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            OrderBottomBar(
                totalPrice = 29500,
                isOrderEnabled = false,
                orderableMessage = "최소 주문 금액은 20,000"
            )
        }
    }
}
