package `in`.koreatech.koin.core.designsystem.component.text

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * 앞에 아이콘이 있는 텍스트
 * @param modifier Modifier
 * @param iconRes 아이콘 리소스
 * @param text 텍스트
 * @param textStyle 텍스트 스타일
 * @param iconSize 아이콘 크기
 * @param iconTint 아이콘 색상
 * @param spacing 아이콘과 텍스트 사이 간격
 */
@Composable
fun LeadingIconText(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    text: String,
    textStyle: TextStyle = KoinTheme.typography.regular14,
    iconSize: Dp = 24.dp,
    iconTint: Color = textStyle.color,
    spacing: Dp = 8.dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeadingIconTextPreview() {
    KoinTheme {
        LeadingIconText(
            iconRes = android.R.drawable.ic_media_play,
            text = "예시예시예시yesi",
        )
    }
}
