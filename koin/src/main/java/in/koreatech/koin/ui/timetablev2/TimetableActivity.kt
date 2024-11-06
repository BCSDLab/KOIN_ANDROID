package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.KeyboardUtils
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.feature.timetable.view.TimetableScreen
import `in`.koreatech.koin.feature.timetable.view.dialog.LectureDuplicationDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.SelectDepartmentDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        getUserExtra { isAnonymous ->
            viewModel.getUser(isAnonymous)
        }
        viewModel.getInitData()
        initComposeView()
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun initComposeView() {
        binding.composeView.setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val lectures by viewModel.lectures.collectAsStateWithLifecycle()
            val searchText by viewModel.searchText.collectAsStateWithLifecycle()
            val department by viewModel.department.collectAsStateWithLifecycle()

            val sheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Collapsed
            )
            val scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = sheetState
            )
            val scope = rememberCoroutineScope()

            hideKeyboard(sheetState.isCollapsed)

            setAppbarEvent {
                scope.launch {
                    if (sheetState.isExpanded) {
                        sheetState.collapse()
                    } else {
                        sheetState.expand()
                    }
                }
            }

            KoinTheme {
                if (uiState.isLectureDuplicationDialogVisible) {
                    LectureDuplicationDialog(
                        onConfirm = viewModel::updateDuplicationTimetableLecture,
                        onDismiss = viewModel::updateIsLectureDuplicationDialogVisible
                    )
                }

                if (uiState.isSelectDepartmentDialogVisible) {
                    SelectDepartmentDialog(
                        department = department,
                        departments = listOf(
                            "건축공학부",
                            "고용서비스정책학과",
                            "기계공학부",
                            "디자인공학부",
                            "메카트로닉스공학부",
                            "산업경영학부",
                            "전기전자통신공학부",
                            "컴퓨터공학부",
                            "화학생명공학부",
                        ),
                        onConfirm = viewModel::updateDepartment,
                        onDismiss = viewModel::updateIsSelectDepartmentDialogVisible
                    )
                }

                TimetableScreen(
                    loading = uiState.loading,
                    range = uiState.range,
                    lectures = lectures,
                    semesters = uiState.semesters,
                    selectedLecture = uiState.selectedLecture,
                    timetableEvents = uiState.timetableEvents,
                    clickedTimetableEvents = uiState.clickedTimetableEvents,
                    searchText = searchText,
                    sheetState = sheetState,
                    scaffoldState = scaffoldState,
                    onSearchTextChange = viewModel::updateSearchText,
                    onClickTimetableSchedule = {}, // TODO : 학기 시간표 선택
                    onClickDownloadTimetable = {}, // TODO : 시간표 다운로드
                    onClickAddLecture = viewModel::updateTimetableLectures,
                    onClickRemoveLecture = viewModel::removeTimetableLectures,
                    onClickLecture = viewModel::updateClickedTimetableEvents,
                    onSelectedLecture = viewModel::updateSelectedLecture,
                    onClickSettingIcon = viewModel::updateIsSelectDepartmentDialogVisible,
                    onClickSearchIcon = {}, // TODO : 검색 아이콘 클릭 (필요할까? 그냥 보여주기용이 좋아보임)
                )
            }
        }
    }

    private fun initEvent() {
        setAppbarEvent()
    }

    private fun getUserExtra(callback: (isAnonymous: Boolean) -> Unit) {
        callback(intent.getBooleanExtra("isAnonymous", true))
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
    }
}
