package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Koin User Info Single Choice Radio Group
 * @param items List of items
 * @param selected Selected item index
 * @param onSelectedItemChange Callback when selected item changes
 * @param modifier [Modifier]
 * @param innerPadding Padding between radio buttons
 */
@Composable
fun KoinUserSingleChoiceRadioGroup(
    items: ImmutableList<String>,
    selected: Int?,
    onSelectedItemChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: Dp = 32.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(innerPadding)
    ) {
        items.forEachIndexed { index, text ->
            KoinUserRadioButton(
                text = text,
                selected = index == selected,
                onSelected = { onSelectedItemChange(index) }
            )
        }
    }
}

/**
 * Koin User Radio Button
 * @param text Trailing text of radio button
 * @param selected Selected state of radio button
 * @param onSelected Callback when selected state changes
 * @param modifier [Modifier]
 */
@Composable
fun KoinUserRadioButton(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.noRippleClickable {
            onSelected()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = KoinTheme.colors.primary500,
                    unselectedColor = KoinTheme.colors.primary500
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular16
        )
    }
}

@Preview
@Composable
private fun KoinUserSingleChoiceRadioGroupPreview() {
    KoinUserSingleChoiceRadioGroup(
        items = persistentListOf("Option 1", "Option 2", "Option 3"),
        selected = 0,
        onSelectedItemChange = {}
    )
}

@Preview
@Composable
private fun KoinUserRadioButtonPreview() {
    KoinUserRadioButton(
        text = "Option 1",
        selected = true,
        onSelected = {}
    )
}
