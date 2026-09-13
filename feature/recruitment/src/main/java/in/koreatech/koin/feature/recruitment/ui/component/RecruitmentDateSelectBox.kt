package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val DateSelectBoxShape = RoundedCornerShape(16.dp)
private val DateSelectBoxPadding = PaddingValues(
    top = 8.dp,
    end = 12.dp,
    bottom = 8.dp,
    start = 12.dp
)

@Composable
fun RecruitmentDateSelectBox(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaceholder: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(DateSelectBoxShape)
            .border(1.dp, RebrandKoinTheme.colors.neutral200, DateSelectBoxShape)
            .background(RebrandKoinTheme.colors.neutral0, DateSelectBoxShape)
            .clickable { onClick() }
            .padding(DateSelectBoxPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.regular14,
            color = if (isPlaceholder) RebrandKoinTheme.colors.neutral400 else RebrandKoinTheme.colors.neutral800
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentDateSelectBoxPreview() {
    RebrandKoinTheme {
        RecruitmentDateSelectBox(
            text = "2026.08.24",
            onClick = {}
        )
    }
}
