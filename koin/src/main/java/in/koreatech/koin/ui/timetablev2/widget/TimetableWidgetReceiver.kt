package `in`.koreatech.koin.ui.timetablev2.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimetableWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TimetableAppWidget

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action

        when (action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val semester = intent.getStringExtra(TimetableAppWidget.SEMESTER) ?: ""
                CoroutineScope(Dispatchers.IO).launch {
                    updateWidget(context, semester)
                }
            }
        }
    }

    suspend fun updateWidget(
        context: Context,
        semester: String
    ) {
        val updatedTime = System.currentTimeMillis().toString()
        GlanceAppWidgetManager(context).getGlanceIds(TimetableAppWidget.javaClass)
            .forEach { glanceId ->
                updateAppWidgetState(context, glanceId) { prefs ->
                    prefs[stringPreferencesKey(TimetableAppWidget.SEMESTER)] = semester
                    prefs[stringPreferencesKey(TimetableAppWidget.LAST_UPDATED)] = updatedTime
                }
            }
        TimetableAppWidget.updateAll(context)
    }
}
