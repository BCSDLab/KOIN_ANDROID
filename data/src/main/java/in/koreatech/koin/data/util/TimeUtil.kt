package `in`.koreatech.koin.data.util

import java.time.LocalTime
import java.time.ZoneId

val nowTime: LocalTime get() = LocalTime.now(ZoneId.of("Asia/Seoul"))

fun Long.toSeconds(): Long = this / 1000