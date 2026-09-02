package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val ButtonShape = RoundedCornerShape(16.dp)
private val DefaultButtonHeight = 40.dp
private val DefaultButtonPadding = PaddingValues(top = 8.dp, end = 12.dp, bottom = 8.dp, start = 12.dp)

@Composable
fun RecruitmentOutlinedActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = DefaultButtonHeight,
    contentPadding: PaddingValues = DefaultButtonPadding
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .border(0.5.dp, RebrandKoinTheme.colors.primary500, ButtonShape)
            .background(RebrandKoinTheme.colors.neutral0, ButtonShape)
            .clickable(enabled = enabled) { onClick() }
            .padding(contentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.medium14,
            color = RebrandKoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentOutlinedActionButtonPreview() {
    RebrandKoinTheme {
        RecruitmentOutlinedActionButton(
            text = "회원정보 불러오기",
            onClick = {}
        )
    }
}
