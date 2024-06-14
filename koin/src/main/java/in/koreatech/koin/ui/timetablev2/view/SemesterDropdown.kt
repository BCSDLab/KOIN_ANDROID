package `in`.koreatech.koin.ui.timetablev2.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.domain.model.timetable.Semester

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SemesterDropdown(
    semesters: List<Semester>,
    modifier: Modifier = Modifier,
    onSemesterTextChanged: (semester: String) -> Unit,
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedText by rememberSaveable {
        mutableStateOf("")
    }

    selectedText.ifBlank {
        if (semesters.isNotEmpty()) semesters[0].format()
        else ""
    }.let {
        selectedText = it
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Black,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                textColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .wrapContentSize()
                .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(4.dp)),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            semesters.forEach { semester ->
                DropdownMenuItem(
                    onClick = {
                        onSemesterTextChanged(semester.semester)
                        selectedText = semester.format()
                        expanded = false
                    }
                ) {
                    Text(
                        text = semester.format(),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SemesterDropdownPreview() {
    val semesters = listOf(
        Semester(1, "20241"),
        Semester(2, "20242"),
    )
    Box(modifier = Modifier.fillMaxSize()) {
        SemesterDropdown(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(4.dp),
            semesters = semesters,
            onSemesterTextChanged = {}
        )
    }
}