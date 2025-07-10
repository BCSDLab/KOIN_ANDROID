package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun QuantitySelectorSection(
    value: Int,
    borderColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(24.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onDecrement,
            colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
            shape = RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.width(32.dp).height(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mage_minus),
                contentDescription = "감소",
                modifier = Modifier.padding(start = 16.dp),
                tint = contentColor
            )
        }
        Text(
            text = value.toString(),
            style = RebrandKoinTheme.typography.bold20,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextButton(
            onClick = onIncrement,
            colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
            shape = RoundedCornerShape(0.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.width(32.dp).height(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "증가",
                modifier = Modifier.padding(end = 16.dp),
                tint = contentColor
            )
        }
    }
}

@Preview
@Composable
fun QuantitySelectorSectionPreview() {
    QuantitySelectorSection(
        value = 1,
        onIncrement = {},
        onDecrement = {},
        modifier = Modifier,
        borderColor = RebrandKoinTheme.colors.primary500,
        contentColor = RebrandKoinTheme.colors.primary500
    )
}
