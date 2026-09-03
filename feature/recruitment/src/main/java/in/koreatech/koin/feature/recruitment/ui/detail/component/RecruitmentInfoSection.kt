package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation

@Composable
fun RecruitmentInfoSection(
    location: RecruitmentLocation,
    activityStartDate: String,
    activityEndDate: String,
    currentParticipants: Int,
    maxParticipants: Int,
    createdAt: String,
    authorNickname: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_location,
            label = stringResource(R.string.recruitment_location_label),
            value = stringResource(location.labelRes)
        )
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_calendar,
            label = stringResource(R.string.recruitment_info_period),
            value = stringResource(
                R.string.recruitment_period_format,
                activityStartDate,
                activityEndDate
            )
        )
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_participants,
            label = stringResource(R.string.recruitment_info_participants),
            value = stringResource(
                R.string.recruitment_participants_count,
                currentParticipants,
                maxParticipants
            )
        )
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_created_at,
            label = stringResource(R.string.recruitment_info_created_at),
            value = createdAt
        )
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_author,
            label = stringResource(R.string.recruitment_info_author),
            value = authorNickname
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentInfoSectionPreview() {
    RebrandKoinTheme {
        RecruitmentInfoSection(
            location = RecruitmentLocation.ONLINE,
            activityStartDate = "2026.07.26",
            activityEndDate = "2026.08.07",
            currentParticipants = 0,
            maxParticipants = 3,
            createdAt = "2026.08.25",
            authorNickname = "코인이"
        )
    }
}
