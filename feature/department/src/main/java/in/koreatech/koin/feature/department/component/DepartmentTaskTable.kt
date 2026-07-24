package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R
import `in`.koreatech.koin.feature.department.state.DepartmentTaskState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val TABLE_BORDER_WIDTH = 0.5.dp
private const val TASK_COLUMN_WEIGHT = 1.25f
private const val PHONE_COLUMN_WEIGHT = 1f

@Composable
internal fun DepartmentTaskTable(
    tasks: ImmutableList<DepartmentTaskState>,
    modifier: Modifier = Modifier,
    onPhoneNumberClick: (DepartmentTaskState) -> Unit = {}
) {
    val borderColor = RebrandKoinTheme.colors.neutral300

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(TABLE_BORDER_WIDTH, borderColor, RoundedCornerShape(20.dp))
    ) {
        TableRow(
            leading = stringResource(R.string.department_table_header_task),
            trailing = stringResource(R.string.department_table_header_phone),
            isHeader = true
        )

        tasks.forEach { task ->
            HorizontalTableDivider()
            TableRow(
                leading = task.name,
                trailing = task.phoneNumber,
                isHeader = false,
                onTrailingClick = { onPhoneNumberClick(task) }
            )
        }
    }
}

@Composable
private fun HorizontalTableDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TABLE_BORDER_WIDTH)
            .background(RebrandKoinTheme.colors.neutral300)
    ) {}
}

@Composable
private fun TableRow(
    leading: String,
    trailing: String,
    isHeader: Boolean,
    modifier: Modifier = Modifier,
    onTrailingClick: () -> Unit = {}
) {
    val textStyle = if (isHeader) {
        RebrandKoinTheme.typography.medium14
    } else {
        RebrandKoinTheme.typography.medium12
    }
    val textColor = if (isHeader) {
        RebrandKoinTheme.colors.neutral600
    } else {
        RebrandKoinTheme.colors.neutral500
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(RebrandKoinTheme.colors.neutral0),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(TASK_COLUMN_WEIGHT)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = leading,
            style = textStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .weight(PHONE_COLUMN_WEIGHT)
                .then(
                    if (isHeader) Modifier else Modifier.noRippleClickable { onTrailingClick() }
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = trailing,
            style = textStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentTaskTablePreview() {
    RebrandKoinTheme {
        DepartmentTaskTable(
            modifier = Modifier.padding(16.dp),
            tasks = persistentListOf(
                DepartmentTaskState("학생지원팀 총괄", "041-560-2530"),
                DepartmentTaskState("학생지도", "041-560-2531"),
                DepartmentTaskState("교외장학(국가장학 등), 학자금대출, 장애학생지원", "041-560-1400")
            )
        )
    }
}