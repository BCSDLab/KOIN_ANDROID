package `in`.koreatech.koin.feature.setting.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun NotificationSwitchSubItem(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.regular16,
    backgroundColor: Color = KoinTheme.colors.neutral0,
    onClick: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            modifier = Modifier.padding(start = 8.dp),
            text = text,
            style = textStyle
        )
        KoinSwitch(
            checked = checked,
            onCheckedChange = onClick
        )
    }
    HorizontalDivider(color = KoinTheme.colors.neutral100)
}

@Preview(showBackground = true)
@Composable
private fun NotificationSwitchSubItemPreview() {
    NotificationSwitchSubItem(
        text = "title",
        checked = false
    )
}