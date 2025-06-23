package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun FilterToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    val backgroundColor = if (checked) RebrandKoinTheme.colors.primary500 else KoinTheme.colors.neutral0
    val contentColor = if (checked) KoinTheme.colors.neutral0 else KoinTheme.colors.neutral400

    Box(
        modifier = modifier
            .height(34.dp)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.4f),
                spotColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.4f),
            )
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 1.dp,
                    shape = shape,
                    ambientColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.2f),
                    spotColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.2f)
                )
                .background(backgroundColor, shape = shape)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = text,
                    style = KoinTheme.typography.bold14,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFCACACA
)
@Composable
fun FilterToggleButtonPreview() {
    FilterToggleButton(
        false,
        onCheckedChange = {},
        iconRes = R.drawable.motorcycle,
        text = "test"
    )
}