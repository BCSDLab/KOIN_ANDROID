package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.DialogProperties
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.DepartmentRadioButton

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
            shape = RebrandKoinTheme.shapes.extraSmall,
            color = Color.White
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                    text = stringResource(id = R.string.select_department_title),
                    style = RebrandKoinTheme.typography.medium18.copy(
                        color = RebrandKoinTheme.colors.primary500,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                DepartmentRadioButtons(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 24.dp)
                        .weight(1f, false)
                        .verticalScroll(rememberScrollState()),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    TextButton(
                        modifier = Modifier.defaultMinSize(minHeight = 1.dp),
                        onClick = {
                            onDismiss(false)
                        },
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = RebrandKoinTheme.colors.neutral0,
                            contentColor = Color(0xFF8B939C)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = RebrandKoinTheme.typography.regular14
                        )
                    }

                    Button(
                        modifier = Modifier.defaultMinSize(minHeight = 1.dp),
                        onClick = {
                            onDismiss(false)
                        },
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RebrandKoinTheme.colors.primary500,
                            contentColor = RebrandKoinTheme.colors.neutral0
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.common_complete),
                            style = RebrandKoinTheme.typography.regular14
                        )
                    }
                }
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
            .wrapContentSize()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        departments.fastForEach { department ->
            key(department) {
                DepartmentRadioButton(
                    modifier = Modifier,
                    text = department,
                    isSelected = department == selectedDepartment,
                    onClick = { onClickDepartment(department) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectDepartmentDialogPreview() {
    RebrandKoinTheme {
        SelectDepartmentDialog(
            department = "",
            departments = emptyList(),
            onConfirm = {},
            onDismiss = {}
        )
    }
}
