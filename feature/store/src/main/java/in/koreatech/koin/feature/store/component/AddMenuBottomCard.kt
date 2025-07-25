package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun AddMenuBottomCard(
    price: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = RebrandKoinTheme.colors.neutral200,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
            .background(color = RebrandKoinTheme.colors.neutral0, shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .padding(vertical = 12.dp, horizontal = 32.dp)
            .navigationBarsPadding()
    ) {
        Button(
            onClick = onClick,
            shape = RebrandKoinTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0
            ),
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.store_cart_add_menu),
                    style = RebrandKoinTheme.typography.regular14
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.price_with_won, price),
                    style = RebrandKoinTheme.typography.bold18
                )
            }
        }
    }
}

@Composable
@Preview
private fun AddMenuBottomCardPreview() {
    RebrandKoinTheme {
        AddMenuBottomCard(price = 5000)
    }
}
