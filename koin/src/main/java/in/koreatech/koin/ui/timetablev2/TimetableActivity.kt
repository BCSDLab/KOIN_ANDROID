package `in`.koreatech.koin.ui.timetablev2

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.showSnackBarWithDismiss
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.KeyboardUtils
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.feature.timetable.model.TimetableConstants.departments
import `in`.koreatech.koin.feature.timetable.state.BottomSheetUI
import `in`.koreatech.koin.feature.timetable.state.TimetableSideEffect
import `in`.koreatech.koin.feature.timetable.utils.BitmapUtils
import `in`.koreatech.koin.feature.timetable.view.TimetableBottomSheetContentMode
import `in`.koreatech.koin.feature.timetable.view.TimetableScreen
import `in`.koreatech.koin.feature.timetable.view.dialog.DeleteLectureDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.LectureDuplicationDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.RequestLoginDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.ScheduleDuplicationDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.SelectDepartmentDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.TimetableTimePickerDialog
import `in`.koreatech.koin.feature.timetable.viewmodel.TimetableViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TimetableActivity : KoinNavigationDrawerActivity() {
    override val screenTitle: String
        get() = SCREEN_TITLE
    override val menuState: MenuState
        get() = MenuState.Timetable

    private lateinit var binding: ActivityTimetableBinding
    private val viewModel: TimetableViewModel by viewModels()

    private val registerTimetableSemesterActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // handle activity result api
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        getUserExtra { isAnonymous ->
            viewModel.updateIsAnonymous(isAnonymous)
        }
        viewModel.getInitData()
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
            val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(TimetableSideEffect.Nothing)
            val customContentState by viewModel.customContentState.collectAsStateWithLifecycle()
            val searchEngineState by viewModel.searchEngineState.collectAsStateWithLifecycle()
            val lectures by viewModel.lectures.collectAsStateWithLifecycle()
            val sheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(sheetState)
            val snackBarHost = remember { SnackbarHostState() }
            val graphicsLayer = rememberGraphicsLayer()
            val scope = rememberCoroutineScope()

            hideKeyboard(sheetState.isCollapsed)

            setAppbarEvent {
                lectures.ifEmpty {
                    viewModel.updateSideEffect(TimetableSideEffect.SnackBar(getString(R.string.timetable_error_no_semester)))
                    return@setAppbarEvent
                }
                scope.launch {
                    if (state.bottomSheetUI == BottomSheetUI.DETAIL) {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                            viewModel.updateBottomSheetUI(BottomSheetUI.DEFAULT)
                            sheetState.expand()
                        } else {
                            viewModel.updateBottomSheetUI(BottomSheetUI.DEFAULT)
                            sheetState.expand()
                        }
                    } else {
                        viewModel.updateBottomSheetUI(BottomSheetUI.DEFAULT)
                        if (sheetState.isExpanded) sheetState.collapse() else sheetState.expand()
                    }
                }
            }

            KoinTheme {
                LaunchedEffect(state.bottomSheetCollapse) {
                    if (state.bottomSheetCollapse) {
                        sheetState.collapse()
                        viewModel.updateBottomSheetCollapse(false)
                    }
                }
                if (dialogState.isLectureDuplicationVisible) {
                    LectureDuplicationDialog(
                        onConfirm = viewModel::updateDuplicationTimetableLecture,
                        onDismiss = viewModel::updateIsLectureDuplicationDialogVisible
                    )
                }

                if (dialogState.isCustomLectureDuplicationVisible) {
                    state.duplicationTimetableEvent?.let {
                        ScheduleDuplicationDialog(
                            timetableEvent = it,
                            onConfirm = viewModel::updateIsCustomLectureDuplicationDialogVisible,
                            onDismiss = viewModel::updateIsCustomLectureDuplicationDialogVisible
                        )
                    }
                }

                if (dialogState.isSelectDepartmentVisible) {
                    SelectDepartmentDialog(
                        department = searchEngineState.department,
                        departments = departments,
                        onConfirm = viewModel::updateDepartment,
                        onDismiss = viewModel::updateIsSelectDepartmentDialogVisible
                    )
                }

                if (dialogState.isLoginVisible) {
                    RequestLoginDialog(
                        onConfirm = {}, // TODO : 로그인 화면으로 연결
                        onDismiss = viewModel::updateIsLoginDialogVisible
                    )
                }

                if (dialogState.isStartTimePickerVisible) {
                    TimetableTimePickerDialog(
                        title = stringResource(R.string.timetable_time_picker_start_title),
                        isStartTime = true,
                        customContent = state.customTimeData,
                        onConfirm = viewModel::updateStarTimeContent,
                        onDismiss = viewModel::updateIsStartTimePickerDialogVisible
                    )
                }

                if (dialogState.isEndTimePickerVisible) {
                    TimetableTimePickerDialog(
                        title = stringResource(R.string.timetable_time_picker_end_title),
                        isStartTime = false,
                        customContent = state.customTimeData,
                        onConfirm = viewModel::updateEndTimeContent,
                        onDismiss = viewModel::updateIsEndTimePickerDialogVisible
                    )
                }

                if (dialogState.isDeleteLectureVisible) {
                    DeleteLectureDialog(
                        lecture = state.deleteLecture,
                        onConfirm = { id ->
                            viewModel.removeTimetableLectureById(id)
                            viewModel.updateIsDeleteLectureDialogVisible(visible = false)
                        },
                        onDismiss = {
                            viewModel.updateIsDeleteLectureDialogVisible(visible = false)
                        }
                    )
                }

                TimetableScreen(
                    range = state.range,
                    loading = state.loading,
                    lectures = lectures,
                    detailLecture = state.detailLecture,
                    semesters = state.semesters,
                    currentSemester = state.currentSemester,
                    selectedLecture = state.selectedLecture,
                    timetableEvents = state.timetableEvents,
                    clickedTimetableEvents = state.clickedTimetableEvents,
                    etcClickedTimetableEvents = state.etcClickedTimetableEvents,
                    searchText = searchEngineState.text,
                    customContents = customContentState,
                    bottomSheetUI = state.bottomSheetUI,
                    bottomSheetContentMode = state.bottomSheetMode,
                    sheetState = sheetState,
                    scaffoldState = bottomSheetScaffoldState,
                    graphicsLayer = graphicsLayer,
                    onSearchTextChange = viewModel::updateSearchText,
                    onClickTimetableSchedule = {
                        startToTimetableSemesterActivity()
                    }, // TODO : 학기 시간표 선택
                    onClickDownloadTimetable = {
                        scope.launch {
                            saveTimetable(graphicsLayer.toImageBitmap().asAndroidBitmap())
                        }
                    },
                    onClickAddLectureMode = viewModel::updateTimetableBottomSheetMode,
                    onClickAddCustomLectureMode = {
                        handleAddCustomLectureMode(state.isAnonymous) {
                            viewModel.updateTimetableBottomSheetMode(
                                TimetableBottomSheetContentMode.CUSTOM
                            )
                        }
                    },
                    onClickBottomSheetComplete = {
                        when (state.bottomSheetMode) {
                            TimetableBottomSheetContentMode.CUSTOM -> {
                                viewModel.updateCustomContent()
                            }

                            TimetableBottomSheetContentMode.BASIC -> {
                                scope.launch { sheetState.collapse() }
                            }
                        }
                    },
                    onClickBottomSheetDetailComplete = {
                        viewModel.updateBottomSheetUI(BottomSheetUI.DETAIL)
                        scope.launch { sheetState.collapse() }
                    },
                    onClickBottomSheetDetailDelete = {
                        viewModel.updateIsDeleteLectureDialogVisible(it, true)
                    },
                    onClickAddLecture = viewModel::updateTimetableLectures,
                    onClickRemoveLecture = viewModel::removeTimetableLectures,
                    onClickLecture = viewModel::updateClickedTimetableEvents,
                    onSelectedLecture = viewModel::updateSelectedLecture,
                    onClickSettingIcon = viewModel::updateIsSelectDepartmentDialogVisible,
                    onClickSearchIcon = {}, // TODO : 검색 아이콘 클릭 (필요할까? 그냥 보여주기용이 좋아보임)
                    onClickTimetableEvent = {
                        scope.launch {
                            if (state.bottomSheetUI == BottomSheetUI.DEFAULT) {
                                if (sheetState.isExpanded) {
                                    sheetState.collapse()
                                    viewModel.updateDetailLectures(it)
                                    sheetState.expand()
                                } else {
                                    viewModel.updateDetailLectures(it)
                                    sheetState.expand()
                                }
                            } else {
                                viewModel.updateDetailLectures(it)
                                if (state.detailLecture?.id != it.id) {
                                    if (sheetState.isExpanded) {
                                        sheetState.collapse()
                                        sheetState.expand()
                                    } else sheetState.expand()
                                } else {
                                    if (sheetState.isCollapsed) sheetState.expand()
                                }
                            }
                        }
                    },
                    onScheduleNameChange = viewModel::updateScheduleTextChange,
                    onProfessorNameChange = viewModel::updateProfessorTextChange,
                    onPlaceNameChange = viewModel::updatePlaceTextChange,
                    onExtraPlaceNameChange = viewModel::updateExtraPlaceByIdTextChange,
                    onDayOfWeekChange = viewModel::updateDayOfWeekChange,
                    onClickStartTime = viewModel::updateIsStartTimePickerDialogVisible,
                    onClickEndTime = viewModel::updateIsEndTimePickerDialogVisible,
                    onClickAddCustomContent = viewModel::addCustomExtraContent,
                    onClickRemoveCustomContent = viewModel::removeCustomExtraContent
                )
            }

            CustomSnackBarHost(snackBarHost)
            LaunchedEffect(sideEffect) {
                when (val effect = sideEffect) {
                    is TimetableSideEffect.SnackBar -> {
                        snackBarHost.showSnackBarWithDismiss(
                            message = effect.message,
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                        viewModel.updateSideEffect(TimetableSideEffect.Nothing)
                    }

                    is TimetableSideEffect.Nothing -> Unit
                }
            }
        }
    }

    private fun initEvent() {
        setAppbarEvent()
    }

    private fun handleAddCustomLectureMode(isAnonymous: Boolean, callback: () -> Unit) {
        if (isAnonymous) {
            viewModel.updateIsLoginDialogVisible(true)
        } else {
            callback()
        }
    }

    private fun getUserExtra(callback: (isAnonymous: Boolean) -> Unit) {
        callback(intent.getBooleanExtra("isAnonymous", true))
    }

    private fun startToTimetableSemesterActivity() {
        val intent = Intent(this, TimetableSemesterActivity::class.java).apply {
            putExtra(BUNDLE_EXTRA_KEY, bundleOf(IS_ANONYMOUS to viewModel.state.value.isAnonymous))
        }
        registerTimetableSemesterActivityResult.launch(intent)
    }

    private fun saveTimetable(bitmap: Bitmap) {
        BitmapUtils(this).saveBitmapImage(bitmap).let {
            if (it) {
                viewModel.updateSideEffect(TimetableSideEffect.SnackBar("이미지 저장중"))
            } else {
                viewModel.updateSideEffect(TimetableSideEffect.SnackBar("이미지 저장 실패"))
            }
        }
    }

    private fun setAppbarEvent(rightButtonClickable: () -> Unit = {}) {
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> rightButtonClickable()
            }
        }
    }

    private fun hideKeyboard(isCollapsed: Boolean) {
        if (isCollapsed) {
            KeyboardUtils(this).hide(binding.root)
        }
    }

    companion object {
        private const val SCREEN_TITLE = "시간표"
        const val BUNDLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
        const val IS_ANONYMOUS = "isAnonymous"
    }
}
