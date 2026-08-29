package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun RecruitmentPostMetaInfo(
    location: String,
    dateRange: String,
    applicantText: String,
    modifier: Modifier = Modifier,
    applicantColor: Color = RebrandKoinTheme.colors.neutral500
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RecruitmentInfoItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_location),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            },
            text = location
        )
        RecruitmentInfoItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_calendar),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            },
            text = dateRange
        )
        RecruitmentInfoItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_user_group),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = applicantColor
                )
            },
            text = applicantText,
            textColor = applicantColor
        )
    }
}
