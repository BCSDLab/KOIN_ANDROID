package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KoinUserBasicItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = { },
    hint: String = "",
    isFieldRequired: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = modifier
    ) {
        Row {
            Text(
                text = title,
                style = KoinTheme.typography.regular16
            )

            Spacer(modifier = Modifier.width(6.dp))

            if (isFieldRequired) {
                Text(
                    text = "*",
                    style = KoinTheme.typography.regular16,
                    color = Color(0xFFC82A2A) // Color code not defined in the design system
                )
            }
        }

        KoinUserBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            enabled = enabled,
            readOnly = readOnly,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLength = maxLength,
            maxLines = maxLines,
            visualTransformation = visualTransformation
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinUserBasicItemPreview() {
    KoinTheme {
        KoinUserBasicItem(
            title = "Name",
            value = "",
            onValueChange = {},
            hint = "asdfasfd"
        )
    }
}
