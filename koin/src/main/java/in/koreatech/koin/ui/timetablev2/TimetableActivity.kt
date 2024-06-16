package `in`.koreatech.koin.ui.timetablev2

import android.os.Bundle
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.compose.ui.TimetableTheme
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.databinding.ActivityTimetableBinding
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.timetablev2.view.TimetableScreen
import `in`.koreatech.koin.util.BitmapUtils
import `in`.koreatech.koin.util.ext.showToast

class TimetableActivity : KoinNavigationDrawerActivity() {
    private lateinit var binding: ActivityTimetableBinding

    override val screenTitle: String
        get() = getString(R.string.navigation_item_timetable)
    override val menuState: MenuState
        get() = MenuState.Timetable

    private var timetableView: MutableState<TimetableView>? = null

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimetableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
        val isAnonymous = intent.getBooleanExtra("isAnonymous", true)

        binding.composeView.setContent {
            TimetableTheme(
                darkTheme = false
            ) {
                val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

                TimetableScreen(
                    isAnonymous = isAnonymous,
                    isKeyboardVisible = isKeyboardVisible,
                    onSavedImage = {
                        BitmapUtils(this).apply {
                            timetableView?.value?.let { view ->
                                capture(view) { bitmap ->
                                    saveBitmapImage(bitmap)
                                }
                            } ?: showToast("retry saved image..")
                        }
                    },
                    content = { bottomSheetState, onEventClick ->
                        TimetableUI(
                            sheetState = bottomSheetState,
                            onEventClick = onEventClick
                        )
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TimetableUI(
        sheetState: BottomSheetState,
        onEventClick: (TimetableEvent) -> Unit,
    ) {
        timetableView = remember {
            mutableStateOf(
                TimetableView(
                    context = this@TimetableActivity,
                    sheetState = sheetState,
                )
            )
        }

        AndroidView(factory = {
            TimetableView(
                context = it,
                sheetState = sheetState,
            ).apply {
                post {
                    timetableView?.value = this
                }
                setOnTimetableEventClickListener { timetableEvent ->
                    onEventClick(timetableEvent)
                }
            }
        })
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