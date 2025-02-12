package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray500

private const val s = "Password visibility"

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 15.sp),
    helperText: String = "",
    errorText: String = "",
    successText: String = "",
    isError: Boolean = false,
    isSuccess: Boolean = false,
) {
    var visible by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(fontSize = 32.sp),
        modifier = modifier.onFocusChanged { focused = it.isFocused },
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        decorationBox = { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(color = ColorTextField),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty())
                                Text(text = label, fontSize = 16.sp, color = ColorHelper)
                            innerTextField()
                        }
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Icon(
                                painter = if(visible) painterResource(id = R.drawable.ic_visibility) else painterResource(id = R.drawable.ic_visibility_off),
                                contentDescription = stringResource(R.string.password_visibility),
                            )
                        }
                    }
                }
                HelperMessage(
                    helperText = helperText,
                    isError = isError,
                    isSuccess = isSuccess,
                    errorText = errorText,
                    successText = successText,
                    focused = focused,
                )
            }

        }
    )
}

@Preview
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        value = "",
        onValueChange = {},
        label = "Password",
        helperText = "Password must be at leasㄱt 8 characters long",
        errorText = "Password must be at least 8 characters long",
        isError = true
    )
}

