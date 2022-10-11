package `in`.koreatech.koin.domain.model.bus

sealed class BusRemainTime {
    data class CityBusRemainTime(
        val busNumber: Int,
        val remainTimeSeconds: Int
    ) : BusRemainTime()

    data class ShuttleBusRemainTime(
        val remainTimeSeconds: Int
    ) : BusRemainTime()

    data class ExpressBusRemainTime(
        val remainTimeSeconds: Int
    ) : BusRemainTime()

    data class CommutingBusRemainTime(
        val remainTimeSeconds: Int
    ) : BusRemainTime()
}