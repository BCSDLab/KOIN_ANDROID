package `in`.koreatech.koin.feature.recruitment.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DateChipShape = RoundedCornerShape(16.dp)

@Composable
fun RecruitmentChatDateChip(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    val pattern = stringResource(R.string.recruitment_chat_date_format)
    val dateText = remember(date, pattern) { date.format(DateTimeFormatter.ofPattern(pattern)) }
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
            text = dateText,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral600
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatDateChipPreview() {
    RecruitmentChatDateChip(date = LocalDate.of(2025, 7, 22))
}
