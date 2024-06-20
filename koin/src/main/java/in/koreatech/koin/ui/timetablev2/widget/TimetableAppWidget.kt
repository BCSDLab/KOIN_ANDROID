package `in`.koreatech.koin.ui.timetablev2.widget

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import `in`.koreatech.koin.model.timetable.TimetableEvent

object TimetableAppWidget : GlanceAppWidget() {
    const val SEMESTER = "semester"
    const val LAST_UPDATED = "lastUpdated"

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext

        provideContent {
            val state = currentState<Preferences>()
            val semester = state[stringPreferencesKey(SEMESTER)] ?: ""
            val lastUpdated = state[stringPreferencesKey(LAST_UPDATED)] ?: ""
            val lecture by remember {
                mutableStateOf("")
            }
            val timetableEvents = remember {
                mutableStateOf<List<TimetableEvent>>(emptyList())
            }

            LaunchedEffect(key1 = lastUpdated) {

            }

            TimetableWidgetScreen(
                state = state
            )
        }
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        TimetableAppWidget.update(context, glanceId)
    }
}
