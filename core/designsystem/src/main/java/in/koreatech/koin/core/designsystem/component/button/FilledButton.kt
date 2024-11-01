package `in`.koreatech.koin.core.designsystem.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = KoinTheme.typography.medium15,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(5.dp),
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = KoinTheme.colors.primary500,
        contentColor = Color.White,
        disabledContainerColor = KoinTheme.colors.neutral300,
        disabledContentColor = KoinTheme.colors.neutral600
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
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