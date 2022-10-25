package `in`.koreatech.koin.domain.model.bus

sealed class BusRemainTime {
    data class CityBusRemainTime(
        val busNumber: Int,
        val remainTimeSeconds: Long
    ) : BusRemainTime()

    data class ShuttleBusRemainTime(
        val remainTimeSeconds: Long
    ) : BusRemainTime()

    data class ExpressBusRemainTime(
        val remainTimeSeconds: Long
    ) : BusRemainTime()

    data class CommutingBusRemainTime(
        val remainTimeSeconds: Long
    ) : BusRemainTime()
}