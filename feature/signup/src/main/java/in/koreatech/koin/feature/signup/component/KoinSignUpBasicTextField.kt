package `in`.koreatech.koin.feature.signup.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import `in`.koreatech.koin.feature.signup.R

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
 * @param visualTransformation [VisualTransformation]
 */
@Composable
fun KoinSignUpBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .padding(vertical = 8.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
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

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(if (value.isEmpty()) 0f else 1f)
                            .noRippleClickable {
                                if (value.isNotEmpty()) {
                                    onValueChange("")
                                }
                            }
                            .fillMaxHeight(),
                        painter = painterResource(id = R.drawable.ic_sign_up_text_field_clean),
                        contentDescription = null
                    )
                }
                HorizontalDivider()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun KoinSignUpBasicTextFieldPreview() {
    KoinSignUpBasicTextField(
        value = "",
        onValueChange = {},
        hint = "asdfasfd"
    )
}
