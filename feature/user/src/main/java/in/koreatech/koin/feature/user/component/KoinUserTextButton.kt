package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R

@Composable
fun KoinUserTextButton(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.noRippleClickable {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = icon,
            contentDescription = text,
            tint = KoinTheme.colors.neutral500
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = KoinTheme.colors.neutral500,
            style = KoinTheme.typography.regular12.copy(
                lineHeightStyle = null // Remove line height
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserTextButtonPreview() {
    KoinUserTextButton(
        text = stringResource(R.string.sign_in_find_login_id),
        icon = painterResource(R.drawable.ic_sign_in_find_login_id),
        onClick = {}
    )
}
