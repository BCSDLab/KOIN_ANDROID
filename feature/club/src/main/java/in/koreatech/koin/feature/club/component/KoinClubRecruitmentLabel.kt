package `in`.koreatech.koin.feature.club.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun KoinClubRecruitmentLabel(
    labelType: KoinClubRecruitmentLabelType,
    dDay: Int? = null
) {
    Box(
        modifier = Modifier
            .background(
                color = when (labelType) {
                    KoinClubRecruitmentLabelType.RECRUITING -> KoinTheme.colors.primary500
                    KoinClubRecruitmentLabelType.RECRUITMENT_ALWAYS -> KoinTheme.colors.primary400
                    KoinClubRecruitmentLabelType.RECRUITMENT_FINISHED -> KoinTheme.colors.primary700
                    KoinClubRecruitmentLabelType.NONE -> Color.Unspecified
                },
                shape = KoinTheme.shapes.extraLarge
            )
            .clip(KoinTheme.shapes.extraLarge)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = if (dDay != null) {
                stringResource(labelType.labesRes, dDay)
            } else {
                stringResource(labelType.labesRes)
            },
            style = KoinTheme.typography.regular10.copy(
                fontSize = 11.sp,
                color = KoinTheme.colors.neutral0
            )
        )
    }
}

@Preview
@Composable
fun KoinClubRecruitmentLabelPreview() {
    KoinTheme {
        KoinClubRecruitmentLabel(
            labelType = KoinClubRecruitmentLabelType.RECRUITING
        )
    }
}

enum class KoinClubRecruitmentLabelType(
    @StringRes val labesRes: Int
) {
    NONE(R.string.club_list_label_none),
    RECRUITING(R.string.club_list_label_recruiting),
    RECRUITMENT_ALWAYS(R.string.club_list_label_recruiting_always),
    RECRUITMENT_FINISHED(R.string.club_list_label_recruiting_finished)
}
