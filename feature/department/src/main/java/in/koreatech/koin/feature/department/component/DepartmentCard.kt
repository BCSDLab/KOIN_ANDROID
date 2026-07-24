package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R
import `in`.koreatech.koin.feature.department.mock.computerScienceOfficeMock
import `in`.koreatech.koin.feature.department.mock.studentSupportTeamMock
import `in`.koreatech.koin.feature.department.state.DepartmentState
import `in`.koreatech.koin.feature.department.state.DepartmentTaskState

@Composable
internal fun DepartmentCard(
    department: DepartmentState,
    modifier: Modifier = Modifier,
    onPhoneNumberClick: (phoneNumber: String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = department.name,
            style = RebrandKoinTheme.typography.bold15,
            color = RebrandKoinTheme.colors.neutral800
        )

        if (!department.isSingleContact) {
            DepartmentTaskTable(
                tasks = department.tasks,
                onPhoneNumberClick = { task: DepartmentTaskState -> onPhoneNumberClick(task.phoneNumber) }
            )
        } else {
            department.singlePhoneNumber?.let { phoneNumber ->
                PhoneNumberRow(
                    phoneNumber = phoneNumber,
                    onCopyClick = { onPhoneNumberClick(phoneNumber) }
                )
            }
        }
    }
}

@Composable
private fun PhoneNumberRow(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    onCopyClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.department_phone_number_format, phoneNumber),
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500
        )

        Row(
            modifier = Modifier.noRippleClickable { onCopyClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.department_copy),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_department_copy),
                contentDescription = stringResource(R.string.department_copy),
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentCardWithTablePreview() {
    RebrandKoinTheme {
        DepartmentCard(
            modifier = Modifier.padding(16.dp),
            department = studentSupportTeamMock
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentCardWithPhoneNumberPreview() {
    RebrandKoinTheme {
        DepartmentCard(
            modifier = Modifier.padding(16.dp),
            department = computerScienceOfficeMock
        )
    }
}