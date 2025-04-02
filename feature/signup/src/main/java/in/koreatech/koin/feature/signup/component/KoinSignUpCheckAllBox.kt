package `in`.koreatech.koin.feature.signup.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.signup.R

/**
 * Koin Sign Up Check All Box Component
 * @param checked Checkbox state
 * @param trailingText Checkbox trailing text
 * @param modifier [Modifier]
 * @param textColor Color of trailing text
 * @param onCheckedChange Callback when checkbox state changes
 */
@Composable
fun KoinSignUpCheckAllBox(
    checked: Boolean,
    trailingText: String,
    modifier: Modifier = Modifier,
    textColor: Color = KoinTheme.colors.primary500,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth().clip(KoinTheme.shapes.extraSmall).background(KoinTheme.colors.neutral100).padding(8.dp),
        verticalAlignment = CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(if (checked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked),
            contentDescription = trailingText,
            modifier = Modifier.noRippleClickable {
                onCheckedChange(!checked)
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.noRippleClickable {
                onCheckedChange(!checked)
            },
            style = KoinTheme.typography.medium14,
            color = textColor,
            text = trailingText
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewKoinSignUpCheckAllBox() {
    KoinSignUpCheckAllBox(
        checked = false,
        trailingText = "전체 동의",
        onCheckedChange = {}
    )
}
