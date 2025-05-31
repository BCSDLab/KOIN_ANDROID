package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * Koin Club Basic Text Field Component
 * @param value [String] value of the text field
 * @param onValueChange Callback when text field value changes
 * @param modifier [Modifier]
 * @param hint [String] hint of the text field
 * @param keyboardOptions [KeyboardOptions]
 * @param keyboardActions [KeyboardActions]
 * @param singleLine Single line flag
 * @param maxLines Maximum lines
 * @param maxLength Maximum length of the text field
 * @param visualTransformation [VisualTransformation]
 */
@Composable
fun KoinClubBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    textStyle: TextStyle = KoinTheme.typography.regular14,
    borderColor: Color = KoinTheme.colors.primary300,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    BasicTextField(
        modifier = modifier
            .border(width = 1.dp, color = borderColor, shape = KoinTheme.shapes.small)
            .background(KoinTheme.colors.neutral100),
        value = value,
        onValueChange = {
            if (it.length < maxLength) {
                onValueChange(it.trim())
            } else {
                onValueChange(it.take(maxLength).trim())
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle.copy(
            lineHeightStyle = null // Remove line height
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                style = textStyle.copy(
                                    lineHeightStyle = null // Remove line height
                                ),
                                color = KoinTheme.colors.neutral700
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinClubBasicTextFieldPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("test")

        Spacer(modifier = Modifier.weight(1f))

        KoinClubBasicTextField(
            value = "",
            onValueChange = {},
            hint = "asdfasfd"
        )
    }
}
