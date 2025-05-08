package `in`.koreatech.koin.feature.signup.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.signup.R

/**
 * Koin Sign Up Password Text Field Component
 * @param value [String] value of the text field
 * @param onValueChange Callback when text field value changes
 * @param modifier [Modifier]
 * @param hint [String] hint of the text field
 * @param singleLine Single line flag
 * @param maxLines Maximum lines
 * @param maxLength Maximum length of the text field
 * @param showPassword Show password flag
 * @param onShowPasswordChange Callback when show password flag changes
 */
@Composable
fun KoinSignUpPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    showPassword: Boolean = false,
    onShowPasswordChange: (Boolean) -> Unit = {}
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            if (it.length < maxLength) {
                onValueChange(it)
            } else {
                onValueChange(it.take(maxLength))
            }
        },
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = keyboardActions,
        textStyle = KoinTheme.typography.regular14.copy(
            lineHeightStyle = null // Remove line height
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(
                mask = '●'
            )
        },
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

                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .noRippleClickable {
                                onShowPasswordChange(!showPassword)
                            },
                        painter = painterResource(id = if (showPassword) R.drawable.ic_sign_up_hide_password else R.drawable.ic_sign_up_show_password),
                        contentDescription = null
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview
@Composable
private fun KoinSignUpPasswordTextFieldPreview() {
    KoinSignUpPasswordTextField(
        value = "",
        onValueChange = {},
        showPassword = true,
        hint = "비밀번호"
    )
}
