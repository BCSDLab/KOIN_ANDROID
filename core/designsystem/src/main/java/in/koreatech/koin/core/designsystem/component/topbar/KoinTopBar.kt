package `in`.koreatech.koin.core.designsystem.component.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun KoinTopBar(
    leadingContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 24.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()

        Spacer(modifier = Modifier.weight(1f))

        trailingContent()
    }
}

@Composable
fun KoinMainTopBar(
    isNewNotificationReceived: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {}
) {
    KoinTopBar(
        modifier = modifier,
        leadingContent = {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_bcsd_symbol),
                contentDescription = null
            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_koin_text),
                contentDescription = null
            )
        },
        trailingContent = {
            Image(
                modifier = Modifier.noRippleClickable(onClick = onNotificationClick),
                imageVector = ImageVector.vectorResource(
                    if (isNewNotificationReceived) {
                        R.drawable.ic_rebrand_notification_dot
                    } else {
                        R.drawable.ic_rebrand_notification
                    }
                ),
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
private fun KoinMainTopBarPreview() {
    RebrandKoinTheme {
        KoinMainTopBar(isNewNotificationReceived = true)
    }
}
