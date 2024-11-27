package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.DepartmentRadioButton
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants.departments

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDepartmentDialog(
    department: String,
    departments: List<String>,
    onConfirm: (String) -> Unit,
    onDismiss: (visible: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDepartment by remember { mutableStateOf(department) }

    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = KoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 12.dp,
                        vertical = 18.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.select_department_title),
                    style = KoinTheme.typography.medium18.copy(
                        color = KoinTheme.colors.primary500
                    )
                )
                DepartmentRadioButtons(
                    departments = departments,
                    selectedDepartment = selectedDepartment,
                    onClickDepartment = {
                        if (selectedDepartment == it) {
                            selectedDepartment = ""
                        } else {
                            selectedDepartment = it
                        }
                    }
                )
                FilledTextButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp)
                        .align(Alignment.End),
                    text = stringResource(id = R.string.common_complete),
                    onClick = { onConfirm(selectedDepartment) }
                )
            }
        }
    }
}

@Composable
fun DepartmentRadioButtons(
    departments: List<String>,
    selectedDepartment: String,
    onClickDepartment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        departments.chunked(2).forEach { rowDepartments ->
            Row(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowDepartments.forEach { department ->
                    DepartmentRadioButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1.0F),
                        text = department,
                        isSelected = department == selectedDepartment,
                        onClick = { onClickDepartment(department) }
                    )
                }
                if (rowDepartments.size == 1)
                    Spacer(modifier = Modifier.weight(1.0F))
            }
        }
    }
}

@Preview
@Composable
private fun SelectDepartmentDialogPreview() {
    KoinTheme {
        SelectDepartmentDialog(
            department = "",
            departments = departments,
            onConfirm = {},
            onDismiss = {},
        )
    }
}

