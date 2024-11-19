package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityTimetableSemesterBinding
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.view.SemesterScreen
import `in`.koreatech.koin.feature.timetable.view.dialog.DeleteSemesterDialog
import `in`.koreatech.koin.feature.timetable.view.dialog.EditSemesterDialogImpl
import `in`.koreatech.koin.feature.timetable.view.dialog.EditTimetableFrameDialog
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
                val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle()
                val userTimetables by viewModel.userTimetableFrames2.collectAsStateWithLifecycle()

                val userSemesters by viewModel.userSemesters2.collectAsStateWithLifecycle()
                val years by viewModel.years.collectAsStateWithLifecycle()

                if (dialogUiState.isEditSemesterDialogVisible) {
                    EditSemesterDialogImpl(
                        years = years,
                        userSemesters = userSemesters,
                        onConfirm = { selectedSemesters ->
                            viewModel.updateSelectedSemesters(selectedSemesters)
                            var isLectureExist = false
                            selectedSemesters.forEach {
                                // TODO::hyeok 강의 유무 확인해서 띄우기
                                if(userTimetables.contains(it) && userTimetables[it]?.isEmpty() == true)
                                    isLectureExist = true
                            }

                            if(isLectureExist) {
                                viewModel.updateDeleteSemesterDialogVisible(true)
                            } else {
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

                SemesterScreen(
                    userTimetables = userTimetables,
                    isAnonymous = isAnonymous,
                    onClickTimetable = ::finishActivityWithResult,
                    onClickAddTimetable = viewModel::onClickAddTimetable,
                    onClickEditTimetable = viewModel::onClickEditTimetable
                )

            }
        }

        binding.timetableListAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> viewModel.updateEditSemesterDialogVisible(true)
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
            if(!viewModel.isAnonymous.value) {
                putInt(TIMETABLE_FRAME_ID, timetableFrame.id)
                putString(TIMETABLE_FRAME_NAME, timetableFrame.timetableName)
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