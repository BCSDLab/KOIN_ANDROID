package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.KeyboardUtils
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.feature.timetable.view.TimetableScreen
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import kotlinx.coroutines.launch

class TimetableActivity : KoinNavigationDrawerActivity() {
    override val screenTitle: String
        get() = SCREEN_TITLE
    override val menuState: MenuState
        get() = MenuState.Timetable

    private lateinit var binding: ActivityTimetableBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initEvent()
    }

    private fun initView() {
        initComposeView()
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun initComposeView() {
        binding.composeView.setContent {
            var searchText by remember { mutableStateOf("") }

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
                TimetableScreen(
                    searchText = searchText,
                    sheetState = sheetState,
                    scaffoldState = scaffoldState,
                    onSearchTextChange = { searchText = it },
                    onClickTimetableSchedule = {}, // TODO : 학기 시간표 선택
                    onClickDownloadTimetable = {} // TODO : 시간표 다운로드
                )
            }
        }
    }

    private fun initEvent() {
        setAppbarEvent()
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
