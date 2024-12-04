package `in`.koreatech.koin.feature.timetable.utils

import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState


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

                editContents = editContents.map {
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
    events.forEach { event ->
        this.data.forEach {content ->
            duplicateWithTimetableEvent(event, content).let {
                if (it) {
                    return event
                }
            }
        }
    }

    return null
}


private fun duplicate(
    content: CustomExtraContentState,
    otherContent: CustomExtraContentState
): Boolean {
    if (content.dayOfWeek == otherContent.dayOfWeek) {
        if (content.startTime == otherContent.startTime ||
            (content.startTime.isAfter(otherContent.startTime) && content.startTime.isBefore(otherContent.endTime)) ||
            content.endTime == otherContent.endTime ||
            (content.endTime.isAfter(otherContent.startTime) && content.endTime.isBefore(otherContent.endTime))
        ) {
            return true
        }
    }
    return false
}

private fun duplicateWithTimetableEvent(
    event: TimetableEvent,
    content: CustomExtraContentState
): Boolean {
    if (event.dayOfWeek == content.dayOfWeek) {
        if (event.start == content.startTime ||
            (event.start.isAfter(content.startTime) && event.start.isBefore(content.endTime)) ||
            event.end == content.endTime ||
            (event.end.isAfter(content.startTime) && event.end.isBefore(content.endTime))
        ) {
            return true
        }
    }
    return false
}
