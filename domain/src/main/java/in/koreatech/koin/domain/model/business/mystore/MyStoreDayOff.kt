package `in`.koreatech.koin.domain.model.business.mystore

data class MyStoreDayOff(
    var closeTime: String?,
    var closed: Boolean,
    var dayOfWeek: String,
    var openTime: String?
    )