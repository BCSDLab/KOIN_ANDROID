package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole

@Composable
fun RecruitmentRoleChip(
    role: RecruitmentRole,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(RebrandKoinTheme.colors.neutral200)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.recruitment_role_item, role.name, role.count),
            style = RebrandKoinTheme.typography.regular10,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentRoleChipPreview() {
    RebrandKoinTheme {
        RecruitmentRoleChip(role = RecruitmentRole("프론트엔드", 1))
    }
}
