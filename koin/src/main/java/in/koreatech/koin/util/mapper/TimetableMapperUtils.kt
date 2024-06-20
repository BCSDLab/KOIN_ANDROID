package `in`.koreatech.koin.util.mapper

import `in`.koreatech.koin.model.timetable.TimeBlock
import `in`.koreatech.koin.model.timetable.TimetableEvent
import java.time.LocalTime

fun List<TimetableEvent>.toTimeBlocks(): List<TimeBlock?> {
    val updateList = Array<TimeBlock?>(10) { null }

    this.forEach {
        if (it.start.hour < 9) return@forEach

        if (it.end.hour > 18) {
            for (i in it.start.hour until 19) {
                if (i == it.start.hour) {
                    updateList[i - 9] = it.convertToTimeBlock(LocalTime.of(19, 0))
                } else {
                    updateList[i - 9] = it.convertToEmptyTimeBlock()
                }
            }
        } else {
            val endHour: Int = if (it.end.minute == 0) it.end.hour
            else it.end.hour + 1

            for (i in it.start.hour until endHour) {
                if (i == it.start.hour) {
                    updateList[i - 9] = it.convertToTimeBlock(it.end)
                } else {
                    updateList[i - 9] = it.convertToEmptyTimeBlock()
                }
            }
        }
    }

    return updateList.toList()
}