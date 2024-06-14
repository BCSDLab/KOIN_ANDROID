package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import `in`.koreatech.koin.R
import `in`.koreatech.koin.compose.ui.TimetableTheme
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.timetablev2.view.TimetableScreen

class TimetableActivity : KoinNavigationDrawerActivity() {
    private lateinit var binding: ActivityTimetableBinding

    override val screenTitle: String
        get() = getString(R.string.navigation_item_timetable)
    override val menuState: MenuState
        get() = MenuState.Timetable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()


        binding.composeView.setContent {
            TimetableTheme {
                TimetableScreen(
                    content = {

                    },
                    sheetContent = {

                    }
                )
            }
        }
    }

    private fun initEvent() {
        handleAppBarEvent()
    }

    private fun handleAppBarEvent() {
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }
    }
}