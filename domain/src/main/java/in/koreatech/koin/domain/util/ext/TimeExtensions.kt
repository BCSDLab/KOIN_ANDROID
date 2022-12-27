package `in`.koreatech.koin.domain.util.ext

val Int.second get() = this * 1000L
val Int.minute get() = this * 60 * 1000L
val Int.hour get() = this * 60 * 60 * 1000L