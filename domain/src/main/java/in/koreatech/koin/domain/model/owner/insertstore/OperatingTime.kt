package `in`.koreatech.koin.domain.model.owner.insertstore

data class OperatingTime(
    val closeTime: String = "",
    val closed: Boolean = false,
    val dayOfWeek: String = "",
    val openTime: String = ""
)