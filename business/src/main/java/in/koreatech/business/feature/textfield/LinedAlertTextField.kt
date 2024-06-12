package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import `in`.koreatech.business.ui.theme.ColorSecondary

@Composable
fun LinedAlertTextField(
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
    showAlert: Boolean = false,
    alertMessage: String = ""
) {
    var isFocused by remember { mutableStateOf(false) }
    Column {
        LinedWhiteTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            modifier = modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            backgroundColor = backgroundColor,
            enabled = enabled,
            textStyle = textStyle,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            showAlert = showAlert
        )

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