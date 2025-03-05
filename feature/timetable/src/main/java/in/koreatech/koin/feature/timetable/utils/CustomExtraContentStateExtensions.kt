package `in`.koreatech.koin.feature.timetable.utils

import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import java.time.LocalTime

fun CustomExtraContentState.calculateEndTime(): LocalTime {
    return if (startTime.isAfter(endTime)) {
        if (startTime == LocalTime.of(23, 30)) {
            startTime.plusMinutes(29)
        } else if (startTime == LocalTime.of(23, 0)) {
            startTime.plusMinutes(59)
        } else {
            startTime.plusHours(1)
        }
    } else {
        if (startTime == endTime) {
            endTime.plusHours(1)
        } else {
            endTime
        }
    }
}

fun CustomExtraContentState.calculateStartTime(): LocalTime {
    return if (startTime.isAfter(endTime) || startTime == endTime) {
        LocalTime.of(9, 0)
    } else {
        startTime
    }
}
