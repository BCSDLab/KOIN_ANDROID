package `in`.koreatech.koin.feature.timetable.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButton
import `in`.koreatech.koin.core.designsystem.component.button.OutlinedBoxButtonColors
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.component.FilledButtonType
import `in`.koreatech.koin.feature.timetable.component.FilledTextButton
import `in`.koreatech.koin.feature.timetable.component.SemesterButton
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType
import java.time.LocalDate


@Composable
fun EditSemesterDialogImpl(
    years: List<Int>,
    userSemesters: List<SemesterModel>,
    isSelectYearDialogVisible: Boolean,
    modifier: Modifier = Modifier,
    onConfirmSelectYear: () -> Unit = {},
    onDismissSelectYear: () -> Unit = {},
    onClickSelectYear: () -> Unit = {},
    onConfirm: (List<SemesterModel>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var selectedSemesters by remember(currentYear, userSemesters) { mutableStateOf(listOf<SemesterType>()) }

    Box(
        modifier = modifier
    ) {
        if (isSelectYearDialogVisible) {
            SelectYearDialog(
                currentYear = currentYear,
                yearList = years,
                onDismiss = onDismissSelectYear,
                onSelectYear = {
                    currentYear = it
                    onConfirmSelectYear()
                }
            )
        } else {
            EditSemesterDialog(
                modifier = Modifier,
                currentYear = currentYear,
                userSemesters = userSemesters.filter { it.year == currentYear }.map { it.type },
                selectedSemesters = selectedSemesters,
                onClickSemester = { clickedSemester ->
                    if (selectedSemesters.contains(clickedSemester))
                        selectedSemesters = selectedSemesters.filter { it != clickedSemester }
                    else
                        selectedSemesters = selectedSemesters + clickedSemester
                },
                onClickYear = onClickSelectYear,
                onConfirm = { onConfirm(selectedSemesters.map { SemesterModel(currentYear, it) }) },
                onDismiss = onDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSemesterDialog(
    currentYear: Int,
    userSemesters: List<SemesterType>,
    selectedSemesters: List<SemesterType>,
    modifier: Modifier = Modifier,
    onClickYear: () -> Unit = {},
    onClickSemester: (SemesterType) -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    BasicAlertDialog(
        modifier = modifier
            .background(color = KoinTheme.colors.neutral0, shape = KoinTheme.shapes.extraSmall),
        onDismissRequest = onDismiss
    ) {
        Box {
            FilledTextButton(
                modifier = Modifier
                    .padding(top = 12.dp, start = 24.dp)
                    .align(Alignment.TopStart)
                    .height(24.dp),
                text = stringResource(id = R.string.semester_edit_year, currentYear),
                textStyle = KoinTheme.typography.medium12.copy(
                    color = KoinTheme.colors.neutral800
                ),
                buttonStyle = FilledButtonType.Neutral,
                contentPadding = PaddingValues(horizontal = 20.dp),
                onClick = onClickYear
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "학기 편집", style = KoinTheme.typography.bold16)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    items(SemesterType.entries) {
                        SemesterButton(
                            text = stringResource(id = it.stringRes),
                            isExisted = userSemesters.contains(it),
                            isSelected = selectedSemesters.contains(it),
                            onClick = { onClickSemester(it) }
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
                        text = stringResource(id = R.string.common_cancellation),
                        colors = OutlinedBoxButtonColors.Neutral,
                        onClick = onDismiss
                    )
                    FilledTextButton(
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.0F),
                        text = stringResource(id = R.string.semester_edit_apply),
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
    BasicAlertDialog(
        modifier = modifier
            .background(color = KoinTheme.colors.neutral0, shape = KoinTheme.shapes.extraSmall)
            .padding(top = 12.dp, start = 24.dp, end = 24.dp, bottom = 40.dp),
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "연도 선택",
                    style = KoinTheme.typography.medium18.copy(
                        color = KoinTheme.colors.primary500
                    )
                )
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = onDismiss
                ) {
                    StableIcon(
                        drawableResId = R.drawable.ic_close,
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
            ) {
                items(yearList) { year ->
                    OutlinedBoxButton(
                        modifier = Modifier
                            .height(40.dp),
                        text = year.toString(),
                        textStyle = KoinTheme.typography.regular15,
                        colors = ButtonColors(
                            containerColor = KoinTheme.colors.neutral0,
                            contentColor = KoinTheme.colors.neutral800,
                            disabledContainerColor = KoinTheme.colors.neutral300,
                            disabledContentColor = KoinTheme.colors.neutral800
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = KoinTheme.colors.neutral300
                        ),
                        enabled = currentYear != year,
                        onClick = { onSelectYear(year) }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun EditSemesterDialogPreview() {
    EditSemesterDialog(
        currentYear = 2024,
        userSemesters = listOf(
            SemesterType.Fall,
            SemesterType.Winter,
        ),
        selectedSemesters = listOf(
            SemesterType.Winter
        ),
        onClickYear = {},
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
        isSelectYearDialogVisible = false,
        onConfirm = { selectedSemester ->
            userSemesters = userSemesters.toMutableList().apply {
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