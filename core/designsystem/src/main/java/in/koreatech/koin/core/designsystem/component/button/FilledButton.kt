package `in`.koreatech.koin.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

enum class FilledButtonColors {
    Primary,
    Warning,
    Danger,
    Success,
}

@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium15,
    shape: Shape = RoundedCornerShape(5.dp),
    enabled: Boolean = true,
    colors: FilledButtonColors = FilledButtonColors.Primary,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    val buttonColors = filledButtonColorByType(type = colors)
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        colors = buttonColors,
        contentPadding = contentPadding,
    ) {
        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium15,
    shape: Shape = RoundedCornerShape(5.dp),
    enabled: Boolean = true,
    colors: ButtonColors,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Composable
@Stable
internal fun filledButtonColorByType(type: FilledButtonColors): ButtonColors =
    when (type) {
        FilledButtonColors.Primary ->
            ButtonColors(
                containerColor = KoinTheme.colors.primary500,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )

        FilledButtonColors.Warning ->
            ButtonColors(
                containerColor = KoinTheme.colors.sub500,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )

        FilledButtonColors.Danger ->
            ButtonColors(
                containerColor = KoinTheme.colors.danger700,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )

        FilledButtonColors.Success ->
            ButtonColors(
                containerColor = KoinTheme.colors.success700,
                contentColor = KoinTheme.colors.neutral0,
                disabledContainerColor = KoinTheme.colors.neutral300,
                disabledContentColor = KoinTheme.colors.neutral600,
            )
    }

@Preview
@Composable
private fun FilledButtonPreview() {
    FilledButton(text = "조회하기", onClick = {})
}

@Preview
@Composable
private fun FilledButtonFullWidthPreview() {
    FilledButton(text = "조회하기", onClick = {}, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
private fun FilledButtonDisabledPreview() {
    FilledButton(text = "조회하기", onClick = {}, enabled = false)
}

@Preview
@Composable
private fun FilledButtonCustomColorPreview() {
    FilledButton(text = "조회하기", onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
private fun FilledButtonWarningPreview() {
    FilledButton(text = "조회하기", onClick = {}, colors = FilledButtonColors.Warning)
}

@Preview
@Composable
private fun FilledButtonDangerPreview() {
    FilledButton(text = "조회하기", onClick = {}, colors = FilledButtonColors.Danger)
}
