package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorTextFieldDescription

@Composable
fun LinedWhiteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    enabled: Boolean = true,
    label: String,
    textStyle: TextStyle = TextStyle.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isAlertRowAvailable: Boolean = true,
    showAlert: Boolean = false,
    alertMessage: String = ""
) {
    var isFocused by remember { mutableStateOf(false) }
    Column {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = modifier
                .width(IntrinsicSize.Min)
                .border(
                    width = 1.dp,
                    color = if (showAlert) ColorSecondary else if (isFocused) ColorPrimary else ColorTextFieldDescription,
                    shape = RoundedCornerShape(5.dp)
                )
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions
        ) {

            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .background(backgroundColor),
                contentAlignment = Alignment.CenterStart
            ) {
                it()
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = label,
                    color = if (value.isEmpty()) ColorTextFieldDescription else Color.Transparent
                )
            }
        }
        if(isAlertRowAvailable)
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
            ) {
                Image(
                    colorFilter = ColorFilter.tint(ColorSecondary),
                    painter = painterResource(R.drawable.exclamation),
                    contentDescription = "Alert",
                    alpha = if (showAlert) 1f else 0f
                )
                Text(
                    modifier = Modifier.padding(start = 1.5.dp),
                    text = if (showAlert) alertMessage else "",
                    color = ColorSecondary,
                )
            }
    }
}