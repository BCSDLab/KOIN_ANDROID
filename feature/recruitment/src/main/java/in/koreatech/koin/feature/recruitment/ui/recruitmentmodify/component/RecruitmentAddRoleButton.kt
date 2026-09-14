package `in`.koreatech.koin.feature.recruitment.ui.recruitmentmodify.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentAddRoleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = if (enabled) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral200,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(top = 6.dp, end = 12.dp, bottom = 6.dp, start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.medium14,
            color = if (enabled) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentAddRoleButtonPreview() {
    RebrandKoinTheme {
        RecruitmentAddRoleButton(
            text = "역할 추가 +",
            onClick = {}
        )
    }
}
