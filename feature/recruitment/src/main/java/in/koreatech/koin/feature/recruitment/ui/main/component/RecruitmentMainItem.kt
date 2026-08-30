package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentItemModel
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentStatus
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecruitmentMainItem(
    item: RecruitmentItemModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RebrandKoinTheme.shapes.extraLarge)
            .background(RebrandKoinTheme.colors.neutral0)
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RecruitmentItemHeader(item = item)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = item.title,
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral700
                )
                if (item.roles.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        item.roles.forEach { role ->
                            RecruitmentChip(text = role)
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RecruitmentMetaItem(
                    iconRes = R.drawable.ic_recruitment_location,
                    text = stringResource(item.location.labelRes)
                )
                RecruitmentMetaItem(
                    iconRes = R.drawable.ic_recruitment_calendar,
                    text = item.period
                )
                RecruitmentMetaItem(
                    iconRes = R.drawable.ic_recruitment_participants,
                    text = stringResource(
                        R.string.recruitment_participants_count,
                        item.currentCount,
                        item.maxCount
                    ),
                    tint = if (item.isFull) {
                        RebrandKoinTheme.colors.primary600
                    } else {
                        RebrandKoinTheme.colors.neutral500
                    }
                )
            }
        }
    }
}

@Composable
private fun RecruitmentItemHeader(
    item: RecruitmentItemModel,
    modifier: Modifier = Modifier
) {
    val typography = RebrandKoinTheme.typography
    val labelStyle = remember(typography) {
        typography.regular10.copy(fontWeight = FontWeight.Medium)
    }
    val (statusText, statusColor) = when (item.status) {
        RecruitmentStatus.RECRUITING -> {
            val dDayText = if (item.dDay <= 0) {
                stringResource(R.string.recruitment_d_day_today)
            } else {
                stringResource(R.string.recruitment_d_day, item.dDay)
            }
            dDayText to RebrandKoinTheme.colors.danger700
        }
        RecruitmentStatus.COMPLETED ->
            stringResource(R.string.recruitment_status_complete) to
                RebrandKoinTheme.colors.primary600
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RecruitmentChip(
            text = stringResource(item.category.labelRes),
            colors = RecruitmentChipDefaults.categoryColors(item.category),
            textStyle = labelStyle
        )
        Text(
            text = statusText,
            style = labelStyle,
            color = statusColor
        )
    }
}

@Composable
private fun RecruitmentMetaItem(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = RebrandKoinTheme.colors.neutral500
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = RebrandKoinTheme.typography.regular10,
            color = tint
        )
    }
}

@Preview
@Composable
private fun RecruitmentMainItemPreview() {
    RebrandKoinTheme {
        RecruitmentMainItem(
            item = RecruitmentItemModel(
                id = 1,
                category = RecruitmentCategory.CONTEST,
                status = RecruitmentStatus.RECRUITING,
                dDay = 5,
                title = "AI 아이디어 공모전 팀원 모집",
                roles = persistentListOf("프론트엔드 1명", "백엔드 1명", "디자인 1명"),
                location = RecruitmentLocation.ONLINE,
                period = "2026.07.26 ~ 2026.08.07",
                currentCount = 0,
                maxCount = 3
            )
        )
    }
}
