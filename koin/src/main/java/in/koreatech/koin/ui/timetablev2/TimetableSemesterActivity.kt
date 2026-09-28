package `in`.koreatech.koin.ui.timetablev2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.component.snackbar.KoinSnackbarDuration
import `in`.koreatech.koin.core.designsystem.component.snackbar.KoinSnackbarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.KoinSnackbarResult
import `in`.koreatech.koin.core.designsystem.component.snackbar.rememberKoinSnackbarHostState
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.toast.ToastUtil
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
import timber.log.Timber

@AndroidEntryPoint
class TimetableSemesterActivity : ComponentActivity() {
    private lateinit var binding: ActivityTimetableSemesterBinding
    private val viewModel by viewModels<SemesterViewModel>()

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.screenState.value.userTimetableFrames.isEmpty()) {
                finishActivityWithResult(
                    semester = "",
                    frameId = -1,
                    timetableName = ""
                )
            } else {
                finishActivityWithResult(
                    semester = viewModel.currentTimetableSemester.value,
                    frameId = viewModel.currentTimetableId.value,
                    timetableName = viewModel.currentTimetableName.value
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdgeWithLightStatusBar()
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableSemesterBinding.inflate(layoutInflater)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        setContentView(binding.root)
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
                val snackBarHost = rememberKoinSnackbarHostState()

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
                        onConfirm = { selectedSemesters ->
                            viewModel.updateSelectedSemesters(selectedSemesters)
                            if (screenState.userSemesters.any { it !in selectedSemesters }) {
                                viewModel.updateDeleteSemesterDialogVisible(true)
                            } else {
                                viewModel.updateEditSemesterDialogVisible(false)
                                viewModel.updateUserSemesters()
                            }
                        },
                        onDismiss = { viewModel.updateEditSemesterDialogVisible(false) }
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
                                SemesterSideEffect.SnackBar(
                                    "${dialogUiState.editedTimetableFrame?.timetableName}가 삭제되었어요"
                                )
                            )
                        }
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
                        }
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
                        }
                    )
                }

                Scaffold(
                    topBar = {
                        KoinTopAppBar2(
                            title = {
                                Text(
                                    text = stringResource(R.string.timetable_semester_title)
                                )
                            },
                            onNavigationIconClick = {
                                onBackPressedDispatcher.onBackPressed()
                            },
                            actions = {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .noRippleClickable {
                                            if (viewModel.screenState.value.isAnonymous) {
                                                viewModel.updateRequestLoginDialogVisible(true)
                                            } else {
                                                viewModel.updateEditSemesterDialogVisible(true)
                                            }
                                        },
                                    imageVector = ImageVector.vectorResource(`in`.koreatech.koin.feature.timetable.R.drawable.ic_edit),
                                    contentDescription = null,
                                    tint = RebrandKoinTheme.colors.neutral800
                                )
                            }
                        )
                    }
                ) { contentPadding ->
                    SemesterScreen(
                        modifier = Modifier.padding(contentPadding),
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
                                    frame
                                )
                            }
                        },
                        onClickLoginText = {
                            viewModel.updateRequestLoginDialogVisible(true)
                        }
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    KoinSnackbarHost(
                        hostState = snackBarHost
                    )
                }

                LaunchedEffect(sideEffect) {
                    when (val effect = sideEffect) {
                        is SemesterSideEffect.SnackBar -> {
                            val result = snackBarHost.showSnackbar(
                                message = effect.message,
                                duration = KoinSnackbarDuration.Short
                            )

                            when (result) {
                                KoinSnackbarResult.Dismissed -> {}
                                KoinSnackbarResult.ActionPerformed -> {
                                    viewModel.restoreTimetableFrame()
                                }
                            }

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
    }

    private fun getIntentBundle(callback: (bundle: Bundle) -> Unit) {
        intent.getBundleExtra(TimetableActivity.BUNDLE_EXTRA_KEY)?.let {
            callback(it)
        } ?: return
    }

    private fun startToLoginActivity() {
        setResult(REQUEST_CODE_LOGIN_ACTIVITY)
        finish()
    }

    private fun finishActivityWithResult(semester: SemesterModel, timetableFrame: TimetableFrame) {
        val intent =
            Intent().apply {
                val bundle =
                    if (!viewModel.screenState.value.isAnonymous) {
                        bundleOf(
                            SEMESTER to semester.toSemester(),
                            TIMETABLE_FRAME_ID to timetableFrame.id,
                            TIMETABLE_FRAME_NAME to timetableFrame.timetableName
                        )
                    } else {
                        bundleOf(
                            SEMESTER to semester.toSemester()
                        )
                    }
                putExtra(BUNDLE_EXTRA_KEY, bundle)
            }

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun finishActivityWithResult(semester: String, frameId: Int, timetableName: String) {
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
                            TIMETABLE_FRAME_NAME to timetableName
                        )
                    } else {
                        bundleOf(
                            SEMESTER to viewModel.currentTimetableSemester.value
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
