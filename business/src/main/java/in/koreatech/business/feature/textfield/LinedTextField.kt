package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextField


@Composable
fun LinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 15.sp),
    isPassword: Boolean = false,
    helperText: String = "",
    errorText: String = "",
    successText: String = "",
    isError: Boolean = false,
    isSuccess: Boolean = false,
) {
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        modifier = modifier.onFocusChanged { focused = it.isFocused },
        maxLines = 1,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Column(modifier = Modifier.fillMaxWidth()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .border(
                            width = 1.dp,
                            color = if (isError) ColorSecondary else if (focused) ColorPrimary else ColorTextField,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(color = ColorTextField, shape = RoundedCornerShape(4.dp)),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier.padding(start = 12.dp),
                    ) {
                        if (value.isEmpty())
                            Text(text = label, fontSize = 14.sp, color = ColorHelper)
                        innerTextField()
                    }

                }

                Box {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = helperText,
                        fontSize = 11.sp,
                        color = ColorHelper,
                    )

                    if (isError) Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = errorText, fontSize = 11.sp, color = ColorSecondary
                    )
                    else if (isSuccess) Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = successText,
                        fontSize = 11.sp,
                        color = ColorPrimary
                    )
                }

            }

        }
    )
}
