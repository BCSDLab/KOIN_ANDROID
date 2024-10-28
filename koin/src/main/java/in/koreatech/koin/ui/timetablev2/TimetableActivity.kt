package `in`.koreatech.koin.ui.timetablev2

import android.content.Intent
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
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.feature.timetable.section.TimetableBottomSheet
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

            setAppbarEvent {
                scope.launch {
                    if (sheetState.isExpanded) {
                        sheetState.collapse()
                    } else {
                        sheetState.expand()
                    }
                }
            }

            MaterialTheme {
                TimetableScreen(
                    searchText = searchText,
                    sheetState = sheetState,
                    scaffoldState = scaffoldState,
                    onSearchTextChange = { searchText = it }
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

    companion object {
        private const val SCREEN_TITLE = "시간표"
    }
}