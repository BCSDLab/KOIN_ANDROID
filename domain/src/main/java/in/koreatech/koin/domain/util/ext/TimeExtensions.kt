package `in`.koreatech.koin.domain.util.ext

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val localTimeNow: LocalTime get() = LocalTime.now(ZoneId.of("Asia/Seoul"))
val localDateTimeNow: LocalDateTime get() = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
val localDayOfWeekName get() = when(localDateTimeNow.dayOfWeek.value) {
    1 -> "MONDAY"
    2 -> "TUESDAY"
    3 -> "WEDNESDAY"
    4 -> "THURSDAY"
    5 -> "FRIDAY"
    6 -> "SATURDAY"
    7 -> "SUNDAY"
    else -> {}
}

val LocalTime.HHMM get() = this.format(DateTimeFormatter.ofPattern("HH:mm"))

// this >= time (17:00 >= 15:00)
fun LocalTime.isEqualOrBigger(time: LocalTime): Boolean =
    this.isAfter(time) || this.equals(time)

// this <= time (17:00 <= 22:00)
fun LocalTime.isEqualOrSmaller(time: LocalTime): Boolean =
    this.isBefore(time) || this.equals(time)

val Int.second get() = this * 1000L
val Int.minute get() = this * 60 * 1000L
val Int.hour get() = this * 60 * 60 * 1000L