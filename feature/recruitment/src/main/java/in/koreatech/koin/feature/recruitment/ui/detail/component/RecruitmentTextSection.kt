package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun RecruitmentTextSection(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    val bullet = stringResource(R.string.recruitment_bullet)
    val bulletedContent = remember(content, bullet) {
        val lines = content.lines().map { it.trim() }.filter { it.isNotEmpty() }
        buildAnnotatedString {
            lines.forEach { line ->
                withStyle(ParagraphStyle(textIndent = TextIndent(restLine = 18.sp))) {
                    append("$bullet  $line")
                }
            }
        }
    }
    if (bulletedContent.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = RebrandKoinTheme.typography.bold14.copy(
                color = RebrandKoinTheme.colors.neutral700
            )
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = bulletedContent,
            style = RebrandKoinTheme.typography.medium12.copy(
                color = RebrandKoinTheme.colors.neutral800
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentTextSectionPreview() {
    RebrandKoinTheme {
        RecruitmentTextSection(
            title = stringResource(R.string.recruitment_section_qualification),
            content = "2학년 이상이면서 학기 중 주 10시간 이상 프로젝트에 참여할 수 있는 사람\n참여율이 높은 사람"
        )
    }
}
