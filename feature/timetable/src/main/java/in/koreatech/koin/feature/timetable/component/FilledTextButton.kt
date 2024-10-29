package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.ThemePreviews

@Composable
fun FilledTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = FilledTextButtonDefaults.textStyle,
    textColor: Color = FilledTextButtonDefaults.textColor,
    colors: ButtonColors = FilledTextButtonDefaults.colors,
    shape: Shape = FilledTextButtonDefaults.shape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = null,
        border = null,
        contentPadding = FilledTextButtonDefaults.contentPadding,
        interactionSource = interactionSource
    ) {
        Text(text = text, style = textStyle, color = textColor)
    }
}

object FilledTextButtonDefaults {
    val contentPadding = PaddingValues(0.dp)

    val colors
        @Composable get() = ButtonColors(
            containerColor = KoinTheme.colors.primary500,
            contentColor = KoinTheme.colors.neutral0,
            disabledContainerColor = KoinTheme.colors.neutral300,
            disabledContentColor = KoinTheme.colors.neutral600
        )
    val shape @Composable get() = KoinTheme.shapes.extraSmall
    val textColor @Composable get() = KoinTheme.colors.neutral0
    val textStyle @Composable get() = KoinTheme.typography.medium15

}

@ThemePreviews
@Composable
private fun FilledTextButtonPreview() {
    KoinTheme {
        FilledTextButton(
            modifier = Modifier.width(96.dp).height(48.dp),
            text = "Preview",
            onClick = {}
        )
    }
}