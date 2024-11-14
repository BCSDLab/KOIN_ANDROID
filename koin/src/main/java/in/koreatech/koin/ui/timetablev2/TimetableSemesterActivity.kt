package `in`.koreatech.koin.ui.timetablev2

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityTimetableSemesterBinding
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.view.SemesterScreen
import `in`.koreatech.koin.feature.timetable.viewmodel.SemesterViewModel
import timber.log.Timber


@AndroidEntryPoint
class TimetableSemesterActivity : ActivityBase() {
    override val screenTitle = SCREEN_TITLE
    private val binding by dataBinding<ActivityTimetableSemesterBinding>()
    private val viewModel by viewModels<SemesterViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_semester)
        getIntentBundle { isAnonymous ->
            Timber.e("isAnonymous : $isAnonymous")
            viewModel.updateIsAnonymous(isAnonymous)
        }

        binding.timetableListComposeView.setContent {
            KoinTheme {
                val dialogUiState by viewModel.dialogUiState.collectAsStateWithLifecycle()
                val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle()
                val userTimetables by viewModel.userTimetableFrames.collectAsStateWithLifecycle()

                val userSemesters by viewModel.userSemesters.collectAsStateWithLifecycle()
                val years by viewModel.years.collectAsStateWithLifecycle()

                SemesterScreen(
                    userTimetables = userTimetables,
                    isAnonymous = isAnonymous,
                    onClickTimetable = ::finishActivityWithResult,
                    onClickAddTimetable = viewModel::onClickAddTimetable,
                    onClickEditTimetable = viewModel::onClickEditTimetable
                )

                if (dialogUiState.isEditSemesterDialogVisible) {
                    // TODO::hyeok 학기 수정 다이얼로그
//                    EditSemesterDialogImpl(
//                        years = years,
//                        userSemesters = userSemesters,
//                        onConfirm = viewModel::onEditSemesters,
//                        onDismiss = { viewModel.updateEditSemesterDialogVisible(false) }
//                    )
                } else if (dialogUiState.isEditTimetableDialogVisible) {
                    // TODO::hyeok  시간표 수정 다이얼로그
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
        intent?.putExtra(
            BUNDLE_EXTRA_KEY,
            bundleOf(
                SEMESTER to semester.toSemester(),
                TIMETABLE_FRAME_ID to timetableFrame.id
            )
        )
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        private const val SCREEN_TITLE = "시간표 목록"
        const val BUNDLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
        const val SEMESTER = "semester"
        const val TIMETABLE_FRAME_ID = "timetableFrameId"
    }
}