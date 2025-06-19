package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KoinUserWithButtonItem(
    title: String,
    value: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = { },
    onButtonAction: () -> Unit = { },
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    buttonEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = KoinTheme.typography.regular16
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            KoinUserBasicTextField(
                modifier = Modifier.weight(1f),
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

            Spacer(Modifier.width(16.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                FilledButton(
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                    text = buttonText,
                    textStyle = KoinTheme.typography.regular10,
                    onClick = onButtonAction,
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                    shape = KoinTheme.shapes.extraSmall,
                    enabled = buttonEnabled
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KoinUserWithButtonItemPreview() {
    KoinTheme {
        KoinUserWithButtonItem(
            title = "Name",
            value = "",
            onValueChange = {},
            hint = "asdfasfd",
            buttonText = "Button"
        )
    }
}
