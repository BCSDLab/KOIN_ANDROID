package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.compose.ui.ColorMain400
import `in`.koreatech.koin.compose.ui.ColorPrimaryMain400_ALPAH10
import `in`.koreatech.koin.domain.model.timetable.Department

@Composable
fun DepartmentBox(
    department: Department,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (Department, Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { onClick(department, selected) }
            .border(
                width = 1.dp,
                color = if (selected) ColorMain400 else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = if (selected) ColorPrimaryMain400_ALPAH10
                else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 6.dp,
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = department.name,
            fontSize = 10.sp,
            color = if (selected) ColorMain400 else Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentBoxPreviewSelected() {
    DepartmentBox(
        department = Department(1, "컴퓨터공학과"),
        selected = true
    ) { _, _ ->

    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentBoxPreview() {
    DepartmentBox(
        department = Department(1, "컴퓨터공학과"),
        selected = false
    ) { _, _ ->

    }
}