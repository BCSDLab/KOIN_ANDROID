package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.ui.timetablev2.component.CustomAlertDialog
import `in`.koreatech.koin.ui.timetablev2.component.DepartmentBox
import `in`.koreatech.koin.ui.timetablev2.component.DepartmentButton
import `in`.koreatech.koin.ui.timetablev2.viewmodel.TimetableViewModel

@Composable
fun DepartmentDialog(
    visible: Boolean,
    selectedDepartments: List<Department>,
    departments: List<Department>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClick: (Department) -> Unit,
    onCompleted: (List<Department>) -> Unit,
) {
    if (visible) {
        CustomAlertDialog(
            onDismissRequest = onDismissRequest
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        vertical = 20.dp,
                        horizontal = 16.dp
                    )
            ) {
                Text(
                    text = "전공선택",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier
                        .background(Color.White)
                ) {
                    itemsIndexed(departments) { index, department ->
                        DepartmentBox(
                            modifier = Modifier.padding(
                                start = if (index % 2 == 0) 0.dp else 2.dp,
                                end = if (index % 2 == 0) 2.dp else 0.dp,
                                bottom = 4.dp
                            ),
                            department = department,
                            selected = selectedDepartments.contains(department),
                            onClick = { selectedDepartment, _ ->
                                onClick(selectedDepartment)
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                DepartmentButton(
                    modifier = Modifier.align(Alignment.End),
                    onCompleted = {
                        onCompleted(selectedDepartments)
                    }
                )
            }
        }
    }
}