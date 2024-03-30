package `in`.koreatech.business.feature_signup.textfield

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorError
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.Orange


@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 15.sp),
    isPassword: Boolean = false,
    isError: Boolean = true,
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
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Box {
                    if (value.isEmpty()) {
                        Text(label, fontSize = 15.sp, color = ColorHelper)
                    }
                    innerTextField()
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        drawLine(
                            color = if (value.isEmpty()) ColorHelper else if (isError) ColorError
                            else Orange,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                }

                if (isError) Text(
                    text = stringResource(id = R.string.verification_code_error),
                    fontSize = 11.sp,
                    color = ColorError
                )

            }


        }
    )
}
