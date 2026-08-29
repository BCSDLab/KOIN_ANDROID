package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentActivityEntry
import `in`.koreatech.koin.feature.recruitment.utils.toDateText
import java.time.LocalDate

private val LabelColumnWidth = 64.dp

@Composable
fun RecruitmentActivityCard(
    activity: RecruitmentActivityEntry,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral50, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = activity.name,
                style = RebrandKoinTheme.typography.medium14,
                color = RebrandKoinTheme.colors.neutral800
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "수정",
                    style = RebrandKoinTheme.typography.medium13,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier
                        .border(1.dp, RebrandKoinTheme.colors.primary500, RoundedCornerShape(20.dp))
                        .noRippleClickable { onEdit() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "삭제",
                    tint = RebrandKoinTheme.colors.neutral500,
                    modifier = Modifier.noRippleClickable { onRemove() }
                )
            }
        }
        Row {
            Text(
                text = "활동 기간",
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500,
                modifier = Modifier.width(LabelColumnWidth)
            )
            Text(
                text = if (activity.isOngoing || activity.endDate == null) {
                    "${activity.startDate.toDateText()} - 진행 중"
                } else {
                    "${activity.startDate.toDateText()} - ${activity.endDate.toDateText()}"
                },
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral700
            )
        }
        Row {
            Text(
                text = "활동 내용",
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500,
                modifier = Modifier.width(LabelColumnWidth)
            )
            Text(
                text = activity.content,
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral700,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentActivityCardPreview() {
    RebrandKoinTheme {
        RecruitmentActivityCard(
            activity = RecruitmentActivityEntry(
                id = 1L,
                name = "AI 공모전",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusMonths(1),
                content = "AI 공모전에서 기획을 담당했고 @@@를 주제로 @@@를 만들었습니다"
            ),
            onRemove = {},
            onEdit = {}
        )
    }
}
