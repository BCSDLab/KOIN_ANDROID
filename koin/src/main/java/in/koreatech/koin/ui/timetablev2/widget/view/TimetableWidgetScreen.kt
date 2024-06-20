package `in`.koreatech.koin.ui.timetablev2.widget.view

import android.appwidget.AppWidgetManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.LocalAppWidgetOptions
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.background
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import `in`.koreatech.koin.model.timetable.TimeBlock

@Composable
fun TimetableWidgetScreen(
    timeBlocks: List<List<TimeBlock?>>,
    modifier: GlanceModifier = GlanceModifier,
    timeWidth: Int = LocalAppWidgetOptions.current.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            TimetableWidgetHeader(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Transparent)
            )
        }
        item {
            TimetableWidgetContent(
                timeWidth = timeWidth.toFloat(),
                timeBlocks = timeBlocks
            )
        }
    }
}

