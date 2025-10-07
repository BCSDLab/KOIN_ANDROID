package `in`.koreatech.koin.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun SettingTitle(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium14,
    backgroundColor: Color = KoinTheme.colors.neutral50
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(vertical = 8.dp, horizontal = 24.dp),
        text = text,
        style = textStyle
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingTitlePreview() {
    SettingTitle(
        text = "일반"
    )
}
