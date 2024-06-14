package `in`.koreatech.koin.ui.timetablev2

import android.content.Context
import android.util.AttributeSet
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.view.Timetable
import `in`.koreatech.koin.ui.timetablev2.viewmodel.TimetableViewModel

class TimetableView @OptIn(ExperimentalMaterialApi::class)
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val sheetState: BottomSheetState,
) : AbstractComposeView(context, attrs, defStyleAttr) {
    lateinit var onTimetableEventClickListener: OnTimetableEventClickListener

    @Composable
    override fun Content() {
        val viewModel = viewModel<TimetableViewModel>()

//        Timetable(
//            events = ,
//            sheetState = sheetState,
//            clickEvent = ,
//            onEventClick = onTimetableEventClickListener::onEventClick
//        )
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