package `in`.koreatech.koin.feature.setting.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.setting.component.switch.KoinSwitch

@Composable
fun NotificationSwitchItem(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    description: String = "",
    textStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionTextStyle: TextStyle = KoinTheme.typography.regular16,
    descriptionColor: Color = KoinTheme.colors.neutral500,
    backgroundColor: Color = KoinTheme.colors.neutral0,
    onClick: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(vertical = 13.dp, horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = text,
                style = textStyle
            )
            KoinSwitch(
                checked = checked,
                onCheckedChange = onClick
            )
        }
        if (description.isNotEmpty()) {
            Spacer(Modifier.height(5.dp))
            BasicText(
                text = description,
                style = descriptionTextStyle.copy(
                    color = descriptionColor
                )
            )
        }
    }
    HorizontalDivider(color = KoinTheme.colors.neutral100)
}

@Preview(showBackground = true)
@Composable
private fun NotificationSwitchItemPreview() {
    NotificationSwitchItem(
        text = "title",
        checked = false
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificationSwitchItemDescriptionPreview() {
    NotificationSwitchItem(
        text = "title",
        checked = true,
        description = "description"
    )
}
