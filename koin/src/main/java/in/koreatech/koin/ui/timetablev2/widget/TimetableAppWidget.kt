package `in`.koreatech.koin.ui.timetablev2.widget

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.fillMaxSize
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.R
import `in`.koreatech.koin.compose.ui.basicColors
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.model.timetable.TimeBlock
import `in`.koreatech.koin.model.timetable.TimetableEvent
import `in`.koreatech.koin.ui.timetablev2.widget.view.TimetableWidgetScreen
import `in`.koreatech.koin.util.ext.toTimetableEvents
import `in`.koreatech.koin.util.mapper.toTimeBlocks
import java.time.DayOfWeek

object TimetableAppWidget : GlanceAppWidget() {
    const val SEMESTER = "semester"
    const val LAST_UPDATED = "lastUpdated"

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val timetableRepository = timetableRepository(context)

        provideContent {
            val state = currentState<Preferences>()
            val semester = state[stringPreferencesKey(SEMESTER)] ?: ""
            val lastUpdated = state[stringPreferencesKey(LAST_UPDATED)] ?: ""
            val timeBlocks = remember {
                mutableStateOf<List<List<TimeBlock?>>>(emptyList())
            }

            LaunchedEffect(key1 = lastUpdated) {
                timetableRepository.getTimetables(semester, true).let { timetables ->
                    timeBlocks.value = generateDayOfWeekTimetables(timetables)
                }
            }

            TimetableWidgetScreen(
                timeBlocks = timeBlocks.value,
                modifier = GlanceModifier.fillMaxSize()
                    .background(imageProvider = ImageProvider(R.drawable.dragon))
            )
        }
    }

    private fun timetableRepository(context: Context): TimetableRepository {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context, TimetableWidgetEntryPoint::class.java
        )
        return hiltEntryPoint.timetableRepository()
    }

    private fun generateTimetableEvents(
        lectures: List<Lecture>,
        colors: List<Color>
    ): List<TimetableEvent> {
        val updatedTimetableEvents = mutableListOf<TimetableEvent>()
        lectures.mapIndexed { index, lecture ->
            lecture.toTimetableEvents(index, colors)
        }.map {
            it.forEach {
                updatedTimetableEvents.add(it)
            }
        }
        return updatedTimetableEvents
    }

    private fun generateDayOfWeekTimetables(timetableEvents: List<Lecture>): List<List<TimeBlock?>> {
        val timeBlocks = MutableList<List<TimeBlock?>>(5) { emptyList() }

        val mondays =
            generateTimetableEvents(
                timetableEvents,
                basicColors
            ).filter { it.dayOfWeek == DayOfWeek.MONDAY }
                .sortedBy { it.start }.toTimeBlocks()
        val tuesdays =
            generateTimetableEvents(
                timetableEvents,
                basicColors
            ).filter { it.dayOfWeek == DayOfWeek.TUESDAY }
                .sortedBy { it.start }.toTimeBlocks()
        val wednesdays =
            generateTimetableEvents(
                timetableEvents,
                basicColors
            ).filter { it.dayOfWeek == DayOfWeek.WEDNESDAY }
                .sortedBy { it.start }.toTimeBlocks()
        val thursday =
            generateTimetableEvents(
                timetableEvents,
                basicColors
            ).filter { it.dayOfWeek == DayOfWeek.THURSDAY }
                .sortedBy { it.start }.toTimeBlocks()
        val fridays =
            generateTimetableEvents(
                timetableEvents,
                basicColors
            ).filter { it.dayOfWeek == DayOfWeek.FRIDAY }
                .sortedBy { it.start }.toTimeBlocks()

        timeBlocks[0] = mondays
        timeBlocks[1] = tuesdays
        timeBlocks[2] = wednesdays
        timeBlocks[3] = thursday
        timeBlocks[4] = fridays

        return timeBlocks
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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TimetableWidgetEntryPoint {
    fun timetableRepository(): TimetableRepository
}