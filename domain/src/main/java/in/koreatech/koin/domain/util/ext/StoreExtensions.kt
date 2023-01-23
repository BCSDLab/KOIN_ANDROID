package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.store.Store
import java.time.LocalTime
import java.time.ZoneId

val Store.isCurrentOpen: Boolean get() {
    val openTimeHour = openTime.hour
    val openTimeMinute = openTime.minute
    val closeTimeHour = closeTime.hour + if(openTime >= closeTime) 24 else 0
    val closeTimeMinute = closeTime.minute
    val now = localTimeNow

    if(now.hour < openTimeHour) return false
    if(now.hour == openTimeHour) return now.minute >= openTimeMinute
    if(now.hour == closeTimeHour) return now.minute <= closeTimeMinute
    if(now.hour > closeTimeHour) return false

    return true //now.hour in open ~ close
}