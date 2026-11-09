package `in`.koreatech.koin.feature.timetable.utils

import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import java.time.LocalTime

internal fun CustomContentState.isValidationPlace(): Boolean {
    val content = this.data
    content.forEach { it ->
        it.place.contains(",").let { isContain -> if (isContain) return false }
    }

    return true
}

// 직접 강의 추가 시, 추가 된 강의 중복 순열로 완전탐색 중복 검증
internal fun CustomContentState.permuteDuplicationLecture(): List<CustomExtraContentState>? {
    val content = this.data

    for (i in 0..<this.data.size) {
        for (j in (i + 1)..<this.data.size) {
            if (duplicate(content[j], content[i])) {
                var editContents = content.toMutableList()

                editContents =
                    editContents.map {
                        if (it.id == content[j].id) {
                            it.copy(isError = true)
                        } else {
                            it
                        }
                    }.toMutableList()

                return editContents
            }
        }
    }

    return null
}

// 직접 강의 추가 시, 기존 강의와 중복 검증
internal fun CustomContentState.duplicationByTimeTableEvents(events: List<TimetableEvent>): TimetableEvent? {
    return events.firstOrNull { event ->
        data.any { content -> duplicateWithTimetableEvent(event, content) }
    }
}

private fun duplicate(
    content: CustomExtraContentState,
    otherContent: CustomExtraContentState
): Boolean =
    content.dayOfWeek == otherContent.dayOfWeek &&
        timeRangesOverlap(
            content.startTime,
            content.endTime,
            otherContent.startTime,
            otherContent.endTime
        )

private fun duplicateWithTimetableEvent(
    event: TimetableEvent,
    content: CustomExtraContentState
): Boolean =
    event.dayOfWeek == content.dayOfWeek &&
        timeRangesOverlap(
            event.start,
            event.end,
            content.startTime,
            content.endTime
        )

private fun timeRangesOverlap(
    start: LocalTime,
    end: LocalTime,
    otherStart: LocalTime,
    otherEnd: LocalTime
): Boolean = start.isBefore(otherEnd) && otherStart.isBefore(end)
