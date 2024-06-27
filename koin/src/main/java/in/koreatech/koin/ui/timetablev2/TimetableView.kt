package `in`.koreatech.koin.ui.timetablev2

import android.content.Context
import android.util.AttributeSet
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.koin.compose.ui.defaultColors
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.view.Timetable
import `in`.koreatech.koin.ui.timetablev2.viewmodel.TimetableViewModel
import `in`.koreatech.koin.util.ext.toTimetableEvents
import org.orbitmvi.orbit.compose.collectAsState

class TimetableView @OptIn(ExperimentalMaterialApi::class)
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val sheetState: BottomSheetState,
) : AbstractComposeView(context, attrs, defStyleAttr) {
    lateinit var onTimetableEventClickListener: OnTimetableEventClickListener

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val viewModel: TimetableViewModel = viewModel()
        val state by viewModel.collectAsState()

        Timetable(
            isKeyboardVisible = state.isKeyboardVisible,
            events = generateTimetableEvents(state.timetableEvents),
            sheetState = sheetState,
            clickEvent = state.lectureEvents,
            onEventClick = onTimetableEventClickListener::onEventClick
        )
    }

    private fun generateTimetableEvents(
        timetableEvents: List<Lecture>,
        colors: List<Color> = defaultColors
    ): List<TimetableEvent> {
        val updateTimetableEvents = mutableListOf<TimetableEvent>()
        timetableEvents.mapIndexed { index, lecture ->
            lecture.toTimetableEvents(index, colors)
        }.map {
            it.forEach {
                updateTimetableEvents.add(it)
            }
        }

        return updateTimetableEvents
    }

    interface OnTimetableEventClickListener {
        fun onEventClick(event: TimetableEvent)
    }

    inline fun setOnTimetableEventClickListener(crossinline onEventClick: (TimetableEvent) -> Unit) {
        this.onTimetableEventClickListener = object : OnTimetableEventClickListener {
            override fun onEventClick(event: TimetableEvent) {
                onEventClick(event)
            }
        }
    }
}