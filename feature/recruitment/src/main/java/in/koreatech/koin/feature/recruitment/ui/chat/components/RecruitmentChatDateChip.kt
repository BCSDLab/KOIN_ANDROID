package `in`.koreatech.koin.feature.recruitment.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val DateChipShape = RoundedCornerShape(16.dp)

@Composable
fun RecruitmentChatDateChip(
    date: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = RebrandKoinTheme.colors.neutral100,
                    shape = DateChipShape
                )
                .padding(vertical = 4.dp, horizontal = 12.dp),
            text = date,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral600
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatDateChipPreview() {
    RecruitmentChatDateChip(date = "2025년 7월 22일")
}
