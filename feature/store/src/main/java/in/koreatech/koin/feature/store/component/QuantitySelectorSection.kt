package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun QuantitySelectorSection(
    value: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = RebrandKoinTheme.colors.primary500,
    contentColor: Color = RebrandKoinTheme.colors.primary500,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_store_quantity_minus),
            contentDescription = stringResource(R.string.store_quantity_minus),
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable {
                    onDecrement()
                },
            tint = contentColor
        )

        Text(
            text = value.toString(),
            style = RebrandKoinTheme.typography.bold20,
            color = contentColor
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_store_quantity_plus),
            contentDescription = stringResource(R.string.store_quantity_plus),
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable {
                    onIncrement()
                },
            tint = contentColor
        )
    }
}

@Preview
@Composable
fun QuantitySelectorSectionPreview() {
    QuantitySelectorSection(
        value = 1
    )
}
