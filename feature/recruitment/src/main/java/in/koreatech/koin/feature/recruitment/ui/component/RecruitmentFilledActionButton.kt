package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val ButtonShape = RoundedCornerShape(16.dp)
private val ButtonHeight = 48.dp
private val ButtonPadding = PaddingValues(top = 8.dp, end = 24.dp, bottom = 8.dp, start = 24.dp)

@Composable
fun RecruitmentFilledActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(ButtonShape)
            .background(
                color = if (enabled) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral200,
                shape = ButtonShape
            )
            .clickable(enabled = enabled, role = Role.Button) { onClick() }
            .padding(ButtonPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.medium16,
            color = if (enabled) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral400
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentFilledActionButtonPreview() {
    RebrandKoinTheme {
        RecruitmentFilledActionButton(
            text = "지원하기",
            onClick = {}
        )
    }
}
