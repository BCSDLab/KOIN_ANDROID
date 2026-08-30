package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model.TeamRecruitmentRole

private val RoleRowItemShape = RoundedCornerShape(8.dp)
private val RoleRowItemPadding = PaddingValues(
    top = 8.dp,
    end = 12.dp,
    bottom = 8.dp,
    start = 12.dp
)
private val StepperBoxWidth = 101.dp

@Composable
fun RecruitmentRoleRow(
    role: TeamRecruitmentRole,
    onNameChange: (String) -> Unit,
    onCountChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, RebrandKoinTheme.colors.neutral200, RoleRowItemShape)
                .background(RebrandKoinTheme.colors.neutral0, RoleRowItemShape)
                .padding(RoleRowItemPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = role.name,
                onValueChange = { newValue ->
                    onNameChange(newValue.take(TeamRecruitmentRole.NAME_MAX_LENGTH))
                },
                singleLine = true,
                textStyle = RebrandKoinTheme.typography.regular14.copy(
                    color = RebrandKoinTheme.colors.neutral800
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${role.name.length}/${TeamRecruitmentRole.NAME_MAX_LENGTH}",
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral600
            )
        }

        Row(
            modifier = Modifier
                .width(StepperBoxWidth)
                .border(1.dp, RebrandKoinTheme.colors.neutral200, RoleRowItemShape)
                .background(RebrandKoinTheme.colors.neutral0, RoleRowItemShape)
                .padding(RoleRowItemPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        if (role.count > TeamRecruitmentRole.MIN_MEMBER_COUNT) onCountChange(role.count - 1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stepper_minus),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.neutral700,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "${role.count}",
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral800,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        if (role.count < TeamRecruitmentRole.MAX_MEMBER_COUNT) onCountChange(role.count + 1)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stepper_plus),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.neutral700,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = RebrandKoinTheme.colors.neutral500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentRoleRowPreview() {
    RebrandKoinTheme {
        RecruitmentRoleRow(
            role = TeamRecruitmentRole(name = "PM", count = 2),
            onNameChange = {},
            onCountChange = {},
            onRemove = {}
        )
    }
}
