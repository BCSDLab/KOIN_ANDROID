package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

enum class FilledButtonType {
    Normal,
    Danger,
    Neutral,
}

@Composable
@Stable
private fun buttonStyleByType(type: FilledButtonType): ButtonColors =
    when (type) {
        FilledButtonType.Normal ->
            ButtonColors(
                containerColor = KoinTheme.colors.primary500,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )

        FilledButtonType.Danger ->
            ButtonColors(
                containerColor = KoinTheme.colors.danger700,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )

        FilledButtonType.Neutral ->
            ButtonColors(
                containerColor = KoinTheme.colors.neutral300,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )
    }

@Composable
fun FilledTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonStyle: FilledButtonType = FilledButtonType.Normal,
    buttonShape: Shape = FilledTextButtonDefaults.shape,
    textStyle: TextStyle = FilledTextButtonDefaults.textStyle,
    contentPadding: PaddingValues = FilledTextButtonDefaults.contentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val buttonColors = buttonStyleByType(type = buttonStyle)
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = buttonShape,
        colors = buttonColors,
        elevation = null,
        border = null,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        Text(text = text, style = textStyle)
    }
}

object FilledTextButtonDefaults {
    val contentPadding = PaddingValues(0.dp)

    val colors
        @Composable get() =
            ButtonColors(
                containerColor = KoinTheme.colors.primary500,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )
    val shape @Composable get() = KoinTheme.shapes.extraSmall
    val textColor @Composable get() = KoinTheme.colors.neutral0
    val textStyle @Composable get() = KoinTheme.typography.medium15
}

@Preview
@Composable
private fun FilledTextButtonNormalPreview() {
    KoinTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            FilledTextButton(
                modifier =
                    Modifier
                        .width(96.dp)
                        .height(48.dp),
                text = "Preview",
                buttonStyle = FilledButtonType.Normal,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun FilledTextButtonDangerPreview() {
    KoinTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            FilledTextButton(
                modifier =
                    Modifier
                        .width(96.dp)
                        .height(48.dp),
                text = "Preview",
                buttonStyle = FilledButtonType.Danger,
                onClick = {},
            )
        }
    }
}
