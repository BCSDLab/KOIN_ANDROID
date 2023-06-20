package `in`.koreatech.koin.domain.util.ext

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

val localTimeNow: LocalTime get() = LocalTime.now(ZoneId.of("Asia/Seoul"))
val localDateTimeNow: LocalDateTime get() = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

val Int.second get() = this * 1000L
val Int.minute get() = this * 60 * 1000L
val Int.hour get() = this * 60 * 60 * 1000L