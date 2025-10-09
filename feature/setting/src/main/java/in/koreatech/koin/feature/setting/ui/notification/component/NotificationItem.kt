package `in`.koreatech.koin.feature.setting.ui.notification.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.setting.R

@Composable
fun NotificationItem(
    text: String,
    modifier: Modifier = Modifier,
    description: String = "",
    textStyle: TextStyle = KoinTheme.typography.medium18,
    descriptionTextStyle: TextStyle = KoinTheme.typography.regular16,
    backgroundColor: Color = KoinTheme.colors.neutral0,
    onClick: () -> Unit = {}
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable {
                onClick()
            }
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
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = ""
            )
        }
        if (description.isNotEmpty()) {
            BasicText(
                text = description,
                style = descriptionTextStyle
            )
        }
    }
    HorizontalDivider(color = KoinTheme.colors.neutral100)
}

@Preview(showBackground = true)
@Composable
private fun NotificationItemPreview() {
    NotificationItem(
        text = "title",
        description = "description"
    )
}