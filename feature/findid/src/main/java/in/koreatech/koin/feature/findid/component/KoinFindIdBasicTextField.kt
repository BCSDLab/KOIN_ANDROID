package `in`.koreatech.koin.feature.findid.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.findid.R

/**
 * Koin Sign Up Basic Text Field Component
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
fun KoinFindIdBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    showTrailingClearButton: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    BasicTextField(
        modifier = modifier,
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
        textStyle = KoinTheme.typography.regular14.copy(
            lineHeightStyle = null // Remove line height
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = hint,
                                style = KoinTheme.typography.regular14.copy(
                                    lineHeightStyle = null // Remove line height
                                ),
                                color = KoinTheme.colors.neutral400
                            )
                        }
                        innerTextField()
                    }

                    if (showTrailingClearButton && enabled && !readOnly) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .alpha(if (value.isEmpty()) 0f else 1f)
                                .noRippleClickable {
                                    if (value.isNotEmpty()) {
                                        onValueChange("")
                                    }
                                },
                            painter = painterResource(id = R.drawable.ic_find_id_text_field_clean),
                            contentDescription = null
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinFindPasswordBasicTextFieldPreview() {
    KoinFindIdBasicTextField(
        value = "",
        onValueChange = {},
        hint = "asdfasfd"
    )
}
