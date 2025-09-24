package `in`.koreatech.koin.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.setting.R

@Composable
fun SettingVersionItem(
    appVersion: String,
    currentVersion: String,
    showVersionInfo: Boolean = false,
    textStyle: TextStyle = KoinTheme.typography.regular16,
    backgroundColor: Color = KoinTheme.colors.neutral0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(
                vertical = if (showVersionInfo) 5.dp else 13.dp,
                horizontal = 24.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.setting_item_app_version),
            style = textStyle
        )
        if (showVersionInfo) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = appVersion,
                    style = KoinTheme.typography.regular14
                )
                Text(
                    text = stringResource(R.string.setting_item_current_version_info, currentVersion),
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500
                )
            }
        }
    }
    HorizontalDivider(color = KoinTheme.colors.neutral100)
}

@Preview(showBackground = true)
@Composable
private fun SettingVersionItemPreviewVersion() {
    SettingVersionItem(
        appVersion = "4.2.3",
        currentVersion = "4.2.2",
        showVersionInfo = true
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingVersionItemPreview() {
    SettingVersionItem(
        appVersion = "4.2.2",
        currentVersion = "4.2.3",
        showVersionInfo = false
    )
}
