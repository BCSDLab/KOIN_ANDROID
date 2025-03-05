package `in`.koreatech.koin.ui.timetablev2

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.showSnackBarWithDismiss
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityTimetableSemesterBinding
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.state.SemesterSideEffect
import `in`.koreatech.koin.feature.timetable.view.SemesterScreen
import `in`.koreatech.koin.feature.timetable.view.dialog.DeleteSemesterDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.EditSemesterDialogImpl
import `in`.koreatech.koin.feature.timetable.view.dialog.EditTimetableFrameDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.RequestLoginDialog
import `in`.koreatech.koin.feature.timetable.viewmodel.ScreenStateUIMode
import `in`.koreatech.koin.feature.timetable.viewmodel.SemesterViewModel
import `in`.koreatech.koin.ui.timetablev2.TimetableActivity.Companion.BUNDLE_LOGIN_EXTRA_KEY
import `in`.koreatech.koin.ui.timetablev2.TimetableActivity.Companion.NAV_TIMETABLE
import timber.log.Timber

@AndroidEntryPoint
class TimetableSemesterActivity : ActivityBase() {
    override val screenTitle = SCREEN_TITLE
    private val binding by dataBinding<ActivityTimetableSemesterBinding>()
    private val viewModel by viewModels<SemesterViewModel>()

    override val onBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.screenState.value.userTimetableFrames.isEmpty()) {
                    finishActivityWithResult(
                        semester = "",
                        frameId = -1,
                        timetableName = "",
                    )
                } else {
                    finishActivityWithResult(
                        semester = viewModel.currentTimetableSemester.value,
                        frameId = viewModel.currentTimetableId.value,
                        timetableName = viewModel.currentTimetableName.value,
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_semester)
        getIntentBundle { bundle ->
            val isAnonymous = bundle.getBoolean(TimetableActivity.IS_ANONYMOUS, false)
            val semester = bundle.getString(TimetableActivity.SEMESTER).orEmpty()
            val frameId = bundle.getInt(TimetableActivity.FRAME_ID)
            val frameName = bundle.getString(TimetableActivity.FRAME_NAME).orEmpty()
            viewModel.updateIntentData(isAnonymous, frameId, semester, frameName)
        }

        binding.timetableListComposeView.setContent {
            KoinTheme {
                val dialogUiState by viewModel.dialogUiState.collectAsStateWithLifecycle()
                val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle()
                val snackBarHost = remember { SnackbarHostState() }

                val screenState by viewModel.screenState.collectAsStateWithLifecycle()

                // TODO::hyeok viewmodel 로 이전
                LaunchedEffect(screenState.userTimetableFrames) {
                    if (screenState.mode == ScreenStateUIMode.IDLE) return@LaunchedEffect
                    if (screenState.userTimetableFrames.isEmpty()) {
                        viewModel.updateScreenMode(ScreenStateUIMode.EMPTY)
                    } else {
                        viewModel.updateScreenMode(ScreenStateUIMode.BASIC)
                    }
                }

                if (screenState.isEditSemesterDialogVisible) {
                    EditSemesterDialogImpl(
                        years = screenState.availableYears,
                        userSemesters = screenState.userSemesters,
                        isSelectYearDialogVisible = screenState.isSelectYearDialogVisible,
                        onConfirmSelectYear = { viewModel.updateSelectYearDialogVisible(false) },
                        onDismissSelectYear = { viewModel.updateSelectYearDialogVisible(false) },
                        onClickSelectYear = { viewModel.updateSelectYearDialogVisible(true) },
                        onConfirm = { selectedSemesters ->
                            viewModel.updateSelectedSemesters(selectedSemesters)
                            if (selectedSemesters.any { it in screenState.userSemesters }) {
                                viewModel.updateDeleteSemesterDialogVisible(true)
                            } else {
                                viewModel.updateEditSemesterDialogVisible(false)
                                viewModel.updateUserSemesters()
                            }
                        },
                        onDismiss = { viewModel.updateEditSemesterDialogVisible(false) },
                    )
                }
                if (screenState.isEditTimetableDialogVisible) {
                    EditTimetableFrameDialog(
                        timetableFrameState = dialogUiState.editedTimetableFrame,
                        onDismiss = { viewModel.updateEditTimetableDialogVisible(false) },
                        onConfirmEdit = {
                            viewModel.editTimetableFrame(it)
                            viewModel.updateEditTimetableDialogVisible(false)
                        },
                        onDeleteFrame = {
                            viewModel.deleteTimetableFrame()
                            viewModel.updateEditTimetableDialogVisible(false)
                            viewModel.updateSideEffect(
                                SemesterSideEffect.SnackBar("${dialogUiState.editedTimetableFrame?.timetableName}가 삭제되었어요"),
                            )
                        },
                    )
                }
                if (screenState.isDeleteSemesterDialogVisible) {
                    DeleteSemesterDialog(
                        onDismiss = {
                            viewModel.updateDeleteSemesterDialogVisible(false)
                            viewModel.updateEditSemesterDialogVisible(false)
                        },
                        onConfirm = {
                            viewModel.updateUserSemesters()
                            viewModel.updateDeleteSemesterDialogVisible(false)
                            viewModel.updateEditSemesterDialogVisible(false)
                        },
                    )
                }
                if (screenState.isRequestLoginDialogVisible) {
                    RequestLoginDialog(
                        onConfirm = {
                            startToLoginActivity()
                            viewModel.updateRequestLoginDialogVisible(false)
                        },
                        onDismiss = {
                            viewModel.updateRequestLoginDialogVisible(false)
                        },
                    )
                }

                SemesterScreen(
                    state = screenState,
                    userTimetables = screenState.userTimetableFrames,
                    isAnonymous = screenState.isAnonymous,
                    onClickTimetable = ::finishActivityWithResult,
                    onClickAddTimetable = {
                        if (screenState.isAnonymous) {
                            viewModel.updateRequestLoginDialogVisible(true)
                        } else {
                            viewModel.onClickAddTimetable(it)
                        }
                    },
                    onClickEditTimetable = { semester, frame ->
                        if (screenState.isAnonymous) {
                            viewModel.updateRequestLoginDialogVisible(true)
                        } else {
                            viewModel.onClickEditTimetable(
                                semester,
                                frame,
                            )
                        }
                    },
                    onClickLoginText = {
                        viewModel.updateRequestLoginDialogVisible(true)
                    },
                )

                CustomSnackBarHost(
                    hotState = snackBarHost,
                    onAction = {
                        viewModel.restoreTimetableFrame()
                    },
                )

                LaunchedEffect(sideEffect) {
                    when (val effect = sideEffect) {
                        is SemesterSideEffect.SnackBar -> {
                            snackBarHost.showSnackBarWithDismiss(
                                message = effect.message,
                                actionLabel = "되돌리기",
                                duration = SnackbarDuration.Short,
                            )
                            viewModel.updateSideEffect(SemesterSideEffect.Nothing)
                        }

                        is SemesterSideEffect.Toast -> {
                            ToastUtil.getInstance().makeShort(effect.message)
                            viewModel.updateSideEffect(SemesterSideEffect.Nothing)
                        }

                        is SemesterSideEffect.Nothing -> Unit
                    }
                }
            }
        }

        binding.timetableListAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> {
                    onBackPressedDispatcher.onBackPressed()
                }

                AppBarBase.getRightButtonId() -> {
                    if (viewModel.screenState.value.isAnonymous) {
                        viewModel.updateRequestLoginDialogVisible(true)
                    } else {
                        viewModel.updateEditSemesterDialogVisible(true)
                    }
                }
            }
        }
    }

    private fun getIntentBundle(callback: (bundle: Bundle) -> Unit) {
        intent.getBundleExtra(TimetableActivity.BUNDLE_EXTRA_KEY)?.let {
            callback(it)
        } ?: return
    }

    private fun startToLoginActivity() {
        val intent =
            Intent().apply {
                putExtra(BUNDLE_LOGIN_EXTRA_KEY, bundleOf(NAV_TIMETABLE to true))
            }
        setResult(REQUEST_CODE_LOGIN_ACTIVITY, intent)
        finish()
    }

    private fun finishActivityWithResult(
        semester: SemesterModel,
        timetableFrame: TimetableFrame,
    ) {
        val intent =
            Intent().apply {
                val bundle =
                    if (!viewModel.screenState.value.isAnonymous) {
                        bundleOf(
                            SEMESTER to semester.toSemester(),
                            TIMETABLE_FRAME_ID to timetableFrame.id,
                            TIMETABLE_FRAME_NAME to timetableFrame.timetableName,
                        )
                    } else {
                        bundleOf(
                            SEMESTER to semester.toSemester(),
                        )
                    }
                putExtra(BUNDLE_EXTRA_KEY, bundle)
            }

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun finishActivityWithResult(
        semester: String,
        frameId: Int,
        timetableName: String,
    ) {
        Timber.d("semester: ${viewModel.currentTimetableSemester.value}")
        Timber.d("Timetable frame id: ${viewModel.currentTimetableId.value}")
        Timber.d("timetable frame name: ${viewModel.currentTimetableName.value}")
        val intent =
            Intent().apply {
                val bundle =
                    if (!viewModel.screenState.value.isAnonymous) {
                        bundleOf(
                            SEMESTER to semester,
                            TIMETABLE_FRAME_ID to frameId,
                            TIMETABLE_FRAME_NAME to timetableName,
                        )
                    } else {
                        bundleOf(
                            SEMESTER to viewModel.currentTimetableSemester.value,
                        )
                    }
                putExtra(BUNDLE_EXTRA_KEY, bundle)
            }

        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        private const val SCREEN_TITLE = "시간표 목록"
        const val BUNDLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
        const val SEMESTER = "semester"
        const val TIMETABLE_FRAME_ID = "timetableFrameId"
        const val TIMETABLE_FRAME_NAME = "timetableFrameName"
        const val REQUEST_CODE_LOGIN_ACTIVITY = 1001
    }
}
