package `in`.koreatech.koin.ui.timetablev2.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class TimetableWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TimetableAppWidget

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action

        when (action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {

            }
        }
    }
}