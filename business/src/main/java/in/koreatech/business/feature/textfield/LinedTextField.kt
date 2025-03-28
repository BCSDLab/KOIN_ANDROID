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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray500
import `in`.koreatech.koin.domain.util.ext.formatTime

@Composable
fun LinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    timerText: Int? = null,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 15.sp),
    isPassword: Boolean = false,
    helperText: String = "",
    errorText: String = "",
    successText: String = "",
    isError: Boolean = false,
    isSuccess: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
) {
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        modifier = modifier.onFocusChanged { focused = it.isFocused },
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(color = ColorTextField, shape = RoundedCornerShape(4.dp)),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(text = label, fontSize = 16.sp, color = ColorHelper)
                            }
                            innerTextField()
                        }
                        if (timerText != null) {
                            Text(
                                text = timerText.formatTime(),
                                fontSize = 16.sp,
                                color = Gray500,
                                modifier = Modifier.padding(top = 4.dp)
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
                    focused = if (timerText != null) true else focused
                )
            }
        }
    )
}

@Preview
@Composable
fun LinedTextFieldPreview() {
    ContactHelperMessage(
        errorText = "Error Text"
    )
}
