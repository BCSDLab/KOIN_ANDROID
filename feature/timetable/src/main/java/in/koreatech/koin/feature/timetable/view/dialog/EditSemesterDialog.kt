package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.DepartmentRadioButton
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType
import java.time.LocalDate

@Composable
fun EditSemesterDialogImpl(
    years: List<Int>,
    userSemesters: List<SemesterModel>,
    modifier: Modifier = Modifier,
    onConfirm: (List<SemesterModel>) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var selectedSemestersByYear by remember(userSemesters) {
        mutableStateOf(
            userSemesters.groupBy { it.year }.mapValues { entry -> entry.value.map { it.type } }
        )
    }
    var isSelectYearVisible by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
    ) {
        if (isSelectYearVisible) {
            SelectYearDialog(
                currentYear = currentYear,
                yearList = years,
                onDismiss = onDismiss,
                onSelectYear = { year ->
                    currentYear = year
                    isSelectYearVisible = false
                }
            )
        } else {
            val selectedSemesters = selectedSemestersByYear[currentYear].orEmpty()

            EditSemesterDialog(
                modifier = Modifier,
                selectedSemesters = selectedSemesters,
                onClickSemester = { semester ->
                    val updated = if (selectedSemesters.contains(semester)) {
                        selectedSemesters - semester
                    } else {
                        selectedSemesters + semester
                    }
                    selectedSemestersByYear = selectedSemestersByYear + (currentYear to updated)
                },
                onConfirm = {
                    onConfirm(
                        selectedSemestersByYear.flatMap { (year, types) ->
                            types.map { SemesterModel(year, it) }
                        }
                    )
                },
                onNavigateToPrevious = { isSelectYearVisible = true },
                onDismiss = onDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSemesterDialog(
    selectedSemesters: List<SemesterType>,
    modifier: Modifier = Modifier,
    onClickSemester: (SemesterType) -> Unit = {},
    onConfirm: () -> Unit = {},
    onNavigateToPrevious: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier = modifier
            .background(color = RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.small)
            .padding(vertical = 20.dp, horizontal = 24.dp),
        onDismissRequest = onDismiss
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "학기 추가(중복 가능)",
                    style = RebrandKoinTheme.typography.medium18.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = RebrandKoinTheme.colors.primary500
                    )
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SemesterType.entries, key = { semester -> semester.format }) { semester ->
                        DepartmentRadioButton(
                            modifier = Modifier,
                            text = stringResource(id = semester.stringRes),
                            isSelected = selectedSemesters.contains(semester),
                            onClick = { onClickSemester(semester) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedBoxButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.semester_dialog_previous),
                        colors = OutlinedBoxButtonColors.Neutral,
                        onClick = onNavigateToPrevious
                    )
                    FilledTextButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.semester_dialog_apply),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectYearDialog(
    currentYear: Int,
    yearList: List<Int>,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onSelectYear: (Int) -> Unit = {}
) {
    val (selectedYear, setSelectedYear) = remember { mutableIntStateOf(currentYear) }

    BasicAlertDialog(
        modifier = modifier
            .background(color = RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.small)
            .padding(vertical = 20.dp, horizontal = 24.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "연도 선택",
                style = RebrandKoinTheme.typography.medium18.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = RebrandKoinTheme.colors.primary500
                )
            )

            LazyColumn(
                modifier = Modifier.height(140.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(yearList, key = { year -> year }) { year ->
                    DepartmentRadioButton(
                        modifier = Modifier,
                        text = year.toString(),
                        isSelected = selectedYear == year,
                        onClick = { setSelectedYear(year) }
                    )
                }
            }

            Row(
                modifier = Modifier.wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedBoxButton(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1.0F),
                    text = stringResource(id = R.string.semester_dialog_cancel),
                    colors = OutlinedBoxButtonColors.Neutral,
                    onClick = onDismiss
                )
                FilledTextButton(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1.0F),
                    text = stringResource(id = R.string.semester_dialog_next),
                    onClick = {
                        onSelectYear(selectedYear)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditSemesterDialogPreview() {
    EditSemesterDialog(
        selectedSemesters = listOf(
            SemesterType.Winter
        ),
        onClickSemester = {},
        onConfirm = {},
        onDismiss = {}
    )
}

@Preview
@Composable
private fun SelectYearDialogContentPreview() {
    SelectYearDialog(currentYear = 2023, yearList = listOf(2019, 2020, 2021, 2022, 2023, 2024).sortedDescending())
}

@Preview
@Composable
private fun EditSemesterDialogImplPreview() {
    var userSemesters by remember { mutableStateOf(listOf<SemesterModel>()) }
    EditSemesterDialogImpl(
        years = listOf(2019, 2020, 2021, 2022, 2023, 2024),
        userSemesters = userSemesters,
        onConfirm = { selectedSemester ->
            userSemesters =
                userSemesters.toMutableList().apply {
                    selectedSemester.forEach {
                        if (contains(it)) {
                            remove(it)
                        } else {
                            add(it)
                        }
                    }
                }
        },
        onDismiss = {}
    )
}
