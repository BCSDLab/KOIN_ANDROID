package `in`.koreatech.koin.ui.timetablev2.view

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.koin.common.UiStatus
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.TimetableSideEffect
import `in`.koreatech.koin.ui.timetablev2.viewmodel.TimetableViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimetableScreen(
    isAnonymous: Boolean,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    timetableViewModel: TimetableViewModel = viewModel(),
    onSavedImage: () -> Unit,
    content: @Composable ColumnScope.(sheetState: BottomSheetState, onEventClick: (TimetableEvent) -> Unit) -> Unit,
) {
    val state by timetableViewModel.collectAsState()

    timetableViewModel.collectSideEffect {
        when (it) {
            is TimetableSideEffect.Toast -> Unit
        }
    }

    LaunchedEffect(key1 = state.semesters) {
        if (state.semesters.isEmpty()) {
            timetableViewModel.loadSemesters()
        }
        if (state.departments.isEmpty()) {
            timetableViewModel.loadDepartments()
        }
    }

    LaunchedEffect(key1 = isAnonymous) {
        timetableViewModel.updateIsAnonymous(isAnonymous)
    }

    if (state.currentSemester.semester.isBlank() && state.semesters.isNotEmpty()) {
        timetableViewModel.updateCurrentSemester(state.semesters[0])
    }


    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BackHandler(sheetState = sheetState)

    DepartmentDialog(
        visible = state.isDepartmentDialogVisible,
        departments = state.departments,
        selectedDepartments = state.selectedDepartments,
        onDismissRequest = {
            if (state.currentDepartments.isEmpty()) {
                timetableViewModel.clearSelectedDepartments()
            }
            timetableViewModel.closeDepartmentDialog()
        },
        onClick = timetableViewModel::updateSelectedDepartment,
        onCompleted = timetableViewModel::updateCurrentDepartment
    )

    LectureAddDialog(
        context = context,
        visible = state.isAddLectureDialogVisible,
        lecture = state.selectedLecture,
        duplication = state.selectedLecture.duplicate(state.timetableEvents),
        onDismissRequest = timetableViewModel::closeAddLectureDialog,
        onAddLecture = { lecture ->
            if (state.selectedLecture.duplicate(state.timetableEvents)) {
                timetableViewModel.duplicateLecture(lecture)
            } else {
                timetableViewModel.addLecture(state.currentSemester, lecture)
            }
            timetableViewModel.closeAddLectureDialog()
//            sendBroadcastReceiver(currentSemester)
        }
    )

    /**
     * 강의 삭제 모달
     */
    LectureRemoveDialog(
        context = context,
        visible = state.isRemoveLectureDialogVisible,
        lecture = state.clickLecture,
        semester = state.currentSemester,
        onDismissRequest = timetableViewModel::closeRemoveLectureDialog,
        onRemoveLecture = timetableViewModel::removeLecture
    )

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            TimetableBottomSheetContent(
                searchText = state.searchText,
                colors = emptyList(),
                lectures = state.lectures,
                selectedLectures = state.selectedLecture,
                currentDepartments = state.currentDepartments,
                onSetting = timetableViewModel::openDepartmentDialog,
                onCancel = timetableViewModel::removeDepartment,
                onAddLecture = timetableViewModel::openAddLectureDialog,
                onSelectedLecture = timetableViewModel::updateSelectedLecture,
                onSearchTextChanged = timetableViewModel::updateSearchText,
                onClickLecture = timetableViewModel::updateLectureEvent
            )
        },
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            TimetableContentHeader(
                semesters = state.semesters,
                modifier = Modifier.fillMaxWidth(),
                onSavedImage = onSavedImage,
                onVisibleBottomSheet = {
                    scope.launch {
                        if (sheetState.isCollapsed) sheetState.expand()
                        else sheetState.collapse()
                    }
                },
                onSemesterTextChanged = timetableViewModel::updateCurrentSemester
            )
            content(
                sheetState = sheetState,
                onEventClick = timetableViewModel::updateClickLecture
            )
        }
    }

    when (state.uiStatus) {
        is UiStatus.Failed -> Unit
        UiStatus.Init -> Unit
        UiStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }

        UiStatus.Success -> Unit
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BackHandler(sheetState: BottomSheetState) {
    val scope = rememberCoroutineScope()

    BackHandler(enabled = sheetState.isExpanded) {
        scope.launch {
            sheetState.collapse()
        }
    }
}

