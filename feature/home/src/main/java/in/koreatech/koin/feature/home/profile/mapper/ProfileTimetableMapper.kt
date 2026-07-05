package `in`.koreatech.koin.feature.home.profile.model

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import java.time.DayOfWeek
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun TimetableLectures.toProfileTimetableLectures(): ImmutableList<ProfileTimetableLecture> {
    val result = mutableListOf<ProfileTimetableLecture>()

    timetable.forEachIndexed { index, lecture ->
        lecture.formatTimetableEventContent().forEach { (day, times, place) ->
            val start = times.firstOrNull() ?: return@forEach
            val lastSlot = times.lastOrNull() ?: return@forEach
            val end = if (lastSlot.hour == 23 && lastSlot.minute == 30) {
                lastSlot.withMinute(59)
            } else {
                lastSlot.plusMinutes(30)
            }

            val dayIndex = day?.toProfileDayIndex() ?: return@forEach

            result += ProfileTimetableLecture(
                name = lecture.classTitle,
                place = place,
                dayOfWeek = dayIndex,
                startTotalMinutes = start.hour * 60 + start.minute,
                endTotalMinutes = end.hour * 60 + end.minute,
                colorIndex = index
            )
        }
    }

    return result.toImmutableList()
}

private fun DayOfWeek.toProfileDayIndex(): Int? = when (this) {
    DayOfWeek.MONDAY -> 0
    DayOfWeek.TUESDAY -> 1
    DayOfWeek.WEDNESDAY -> 2
    DayOfWeek.THURSDAY -> 3
    DayOfWeek.FRIDAY -> 4
    else -> null
}

fun emptyProfileTimetableLectures(): ImmutableList<ProfileTimetableLecture> = persistentListOf()
