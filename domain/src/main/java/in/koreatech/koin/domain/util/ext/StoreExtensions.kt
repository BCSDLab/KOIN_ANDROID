package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.store.Store
import java.time.LocalTime
import java.time.ZoneId

val Store.isCurrentOpen get() = localTimeNow in openTime..closeTime