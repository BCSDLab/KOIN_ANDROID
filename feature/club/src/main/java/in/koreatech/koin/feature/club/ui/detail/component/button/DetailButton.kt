package `in`.koreatech.koin.feature.club.ui.detail.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DetailButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    textStyle: TextStyle = KoinTheme.typography.medium14,
    shape: Shape = RoundedCornerShape(4.dp),
    enabled: Boolean = true,
    colors: ButtonColors = ButtonColors(
        containerColor = KoinTheme.colors.primary500,
        contentColor = KoinTheme.colors.neutral0,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    ),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
) {
    Box(
        modifier = modifier
            .background(
                color = if (enabled) colors.containerColor else colors.disabledContainerColor,
                shape = shape
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            color = if (enabled) colors.contentColor else colors.disabledContentColor,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Preview
@Composable
fun DetailButtonPreview() {
    DetailButton(
        text = "상세 소개 수정",
        onClick = {}
    )
}
