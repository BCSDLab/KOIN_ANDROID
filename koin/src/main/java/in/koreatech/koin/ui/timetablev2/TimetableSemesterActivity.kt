package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityTimetableSemesterBinding
import `in`.koreatech.koin.feature.timetable.view.SemesterScreen
import `in`.koreatech.koin.feature.timetable.viewmodel.SemesterViewModel


@AndroidEntryPoint
class TimetableSemesterActivity : ActivityBase() {
    override val screenTitle = SCREEN_TITLE
    private val binding by dataBinding<ActivityTimetableSemesterBinding>()
    private val viewModel by viewModels<SemesterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable_semester)

        binding.timetableListComposeView.setContent {
            KoinTheme {
                val dialogUiState by viewModel.dialogUiState.collectAsStateWithLifecycle()
                val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle()
                val userTimetables by viewModel.userTimetableFrames.collectAsStateWithLifecycle()

                SemesterScreen(
                    userTimetables = userTimetables,
                    isAnonymous = isAnonymous,
                    onClickTimetable = viewModel::onClickTimetable,
                    onClickAddTimetable = viewModel::onClickAddTimetable,
                    onClickEditTimetable = viewModel::onClickEditTimetable
                )

                if(dialogUiState.isEditSemesterDialogVisible) {
                    // TODO::hyeok 학기 수정 다이얼로그
                } else if(dialogUiState.isEditTimetableDialogVisible) {
                    // TODO::hyeok  시간표 수정 다이얼로그
                }
            }
        }
    }

    companion object {
        private const val SCREEN_TITLE = "시간표 목록"
    }
}