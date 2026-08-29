package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.detail.model.RecruitmentRoleModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecruitmentRoleSection(
    roles: ImmutableList<RecruitmentRoleModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.recruitment_section_roles),
            style = RebrandKoinTheme.typography.bold14.copy(
                color = RebrandKoinTheme.colors.neutral700
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = RebrandKoinTheme.colors.neutral300,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            roles.forEachIndexed { index, role ->
                key(role.id) {
                    if (index > 0) {
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = RebrandKoinTheme.colors.neutral300
                        )
                    }
                    RecruitmentDetailRow(
                        modifier = Modifier
                            .height(32.dp)
                            .padding(horizontal = 10.dp),
                        iconRes = R.drawable.ic_recruitment_role,
                        label = role.name,
                        value = if (role.isClosed) {
                            stringResource(R.string.recruitment_closed)
                        } else {
                            stringResource(
                                R.string.recruitment_role_participants_format,
                                role.maxParticipants
                            )
                        },
                        iconTint = if (role.isClosed) {
                            RebrandKoinTheme.colors.neutral500
                        } else {
                            RebrandKoinTheme.colors.primary600
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentRoleSectionPreview() {
    RebrandKoinTheme {
        RecruitmentRoleSection(
            roles = persistentListOf(
                RecruitmentRoleModel(id = 1, name = "프론트엔드", maxParticipants = 1, isClosed = true),
                RecruitmentRoleModel(id = 2, name = "백엔드", maxParticipants = 1),
                RecruitmentRoleModel(id = 3, name = "디자인", maxParticipants = 1)
            )
        )
    }
}
