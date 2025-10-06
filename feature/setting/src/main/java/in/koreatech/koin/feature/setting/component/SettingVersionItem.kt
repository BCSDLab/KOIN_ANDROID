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
    currentVersion: String,
    latestVersion: String,
    showVersionInfo: Boolean,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.regular16,
    backgroundColor: Color = KoinTheme.colors.neutral0
) {
    Row(
        modifier = modifier
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
                    text = currentVersion,
                    style = KoinTheme.typography.regular14
                )
                Text(
                    text = if (currentVersion == latestVersion) {
                        stringResource(R.string.setting_item_newest_version_info)
                    } else {
                        stringResource(R.string.setting_item_not_newest_version_info, latestVersion)
                    },
                    style = KoinTheme.typography.regular12,
                    color = if (currentVersion == latestVersion) {
                        KoinTheme.colors.neutral500
                    } else {
                        KoinTheme.colors.primary500
                    }
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
        currentVersion = "4.2.3",
        latestVersion = "4.2.4",
        showVersionInfo = true
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingVersionItemPreviewLatest() {
    SettingVersionItem(
        currentVersion = "4.2.2",
        latestVersion = "4.2.2",
        showVersionInfo = true
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingVersionItemPreviewNotShow() {
    SettingVersionItem(
        currentVersion = "4.2.2",
        latestVersion = "4.2.2",
        showVersionInfo = false
    )
}
