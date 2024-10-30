package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.feature.timetable.view.TimetableScreen
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState

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

        binding.composeView.setContent {
            MaterialTheme {
                //TimetableScreen()
            }
        }
    }

    companion object {
        private const val SCREEN_TITLE = "시간표"
    }
}