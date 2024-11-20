package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
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
import `in`.koreatech.koin.feature.timetable.viewmodel.SemesterViewModel
import timber.log.Timber


@AndroidEntryPoint
class TimetableSemesterActivity : ActivityBase() {
    override val screenTitle = SCREEN_TITLE
    private val binding by dataBinding<ActivityTimetableSemesterBinding>()
    private val viewModel by viewModels<SemesterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_semester)
        getIntentBundle { isAnonymous ->
            Timber.e("isAnonymous : $isAnonymous")
            viewModel.updateIsAnonymous(false)
        }
        viewModel.initData()

        binding.timetableListComposeView.setContent {
            KoinTheme {
                val dialogUiState by viewModel.dialogUiState.collectAsStateWithLifecycle()
                val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle()
                val snackBarHost = remember { SnackbarHostState() }

                val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle()
                val userTimetables by viewModel.userTimetableFrames.collectAsStateWithLifecycle()
                val userSemesters by viewModel.userSemesters.collectAsStateWithLifecycle()
                val years by viewModel.years.collectAsStateWithLifecycle()

                if (dialogUiState.isEditSemesterDialogVisible) {
                    EditSemesterDialogImpl(
                        years = years,
                        userSemesters = userSemesters,
                        onConfirm = { selectedSemesters ->
                            viewModel.updateSelectedSemesters(selectedSemesters)
                            if (selectedSemesters.any { it in userSemesters })
                                viewModel.updateDeleteSemesterDialogVisible(true)
                            else {
                                viewModel.updateEditSemesterDialogVisible(false)
                                viewModel.updateUserSemesters()
                            }
                        },
                        onDismiss = { viewModel.updateEditSemesterDialogVisible(false) }
                    )
                }
                if (dialogUiState.isEditTimetableDialogVisible) {
                    EditTimetableFrameDialog(
                        timetableFrameState = dialogUiState.editedTimetableFrame,
                        onDismiss = { viewModel.updateEditTimetableDialogVisibility(false) },
                        onConfirmEdit = {
                            viewModel.editTimetableFrame(it)
                            viewModel.updateEditTimetableDialogVisibility(false)
                        },
                        onDeleteFrame = {
                            viewModel.deleteTimetableFrame()
                            viewModel.updateEditTimetableDialogVisibility(false)
                            viewModel.updateSideEffect(SemesterSideEffect.SnackBar("${dialogUiState.editedTimetableFrame?.timetableName}가 삭제되었어요"))
                        }
                    )
                }
                if (dialogUiState.isDeleteSemesterDialogVisible) {
                    DeleteSemesterDialog(
                        onDismiss = {
                            viewModel.updateDeleteSemesterDialogVisible(false)
                            viewModel.updateEditSemesterDialogVisible(false)
                        },
                        onConfirm = {
                            viewModel.updateUserSemesters()
                            viewModel.updateDeleteSemesterDialogVisible(false)
                            viewModel.updateEditSemesterDialogVisible(false)
                        }
                    )
                }
                if (dialogUiState.isRequestLoginDialogVisible) {
                    RequestLoginDialog(
                        onConfirm = {
                            // TODO::Hyeok 로그인 화면으로 이동
                            Timber.d("로그인 화면으로 이동")
                            viewModel.updateRequestLoginDialogVisible(false)
                        },
                        onDismiss = {
                            viewModel.updateRequestLoginDialogVisible(false)
                        }
                    )
                }

                SemesterScreen(
                    userTimetables = userTimetables,
                    isAnonymous = isAnonymous,
                    onClickTimetable = ::finishActivityWithResult,
                    onClickAddTimetable = {
                        if (viewModel.isAnonymous.value) {
                            viewModel.updateRequestLoginDialogVisible(true)
                        } else {
                            viewModel.onClickAddTimetable(it)
                        }
                    },
                    onClickEditTimetable = { semester, frame ->
                        if (viewModel.isAnonymous.value) {
                            viewModel.updateRequestLoginDialogVisible(true)
                        } else {
                            viewModel.onClickEditTimetable(
                                semester,
                                frame
                            )
                        }
                    },
                    onClickLoginText = {
                        viewModel.updateRequestLoginDialogVisible(true)
                    }
                )

                CustomSnackBarHost(
                    hotState = snackBarHost,
                    radius = 6.dp,
                    messageTextStyle = KoinTheme.typography.regular14.copy(
                        color = KoinTheme.colors.neutral0
                    ),
                    actionLabelTextStyle = KoinTheme.typography.regular14.copy(
                        color = KoinTheme.colors.sub500
                    ),
                    background = KoinTheme.colors.primary700,
                    innerPaddingValues = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                    onAction = {
                        viewModel.restoreTimetableFrame()
                    }
                )

                LaunchedEffect(sideEffect) {
                    when (val effect = sideEffect) {
                        is SemesterSideEffect.SnackBar -> {
                            snackBarHost.showSnackBarWithDismiss(
                                message = effect.message,
                                actionLabel = "되돌리기",
                                duration = SnackbarDuration.Short
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
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> {
                    if (viewModel.isAnonymous.value) {
                        viewModel.updateRequestLoginDialogVisible(true)
                    } else {
                        viewModel.updateEditSemesterDialogVisible(true)
                    }
                }
            }
        }
    }

    private fun getIntentBundle(callback: (isAnonymous: Boolean) -> Unit) {
        val bundle = intent.getBundleExtra(TimetableActivity.BUNDLE_EXTRA_KEY)
        bundle?.getBoolean(TimetableActivity.IS_ANONYMOUS)?.let {
            callback(it)
        }
    }

    private fun finishActivityWithResult(semester: SemesterModel, timetableFrame: TimetableFrame) {
        bundleOf().apply {
            putString(SEMESTER, semester.toSemester())
            putString(TIMETABLE_FRAME_NAME, timetableFrame.timetableName)
            if (!viewModel.isAnonymous.value) {
                putInt(TIMETABLE_FRAME_ID, timetableFrame.id)
            }
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
    }
}