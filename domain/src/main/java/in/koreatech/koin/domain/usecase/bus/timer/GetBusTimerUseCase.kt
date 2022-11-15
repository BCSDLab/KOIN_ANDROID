package `in`.koreatech.koin.domain.usecase.bus.timer

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.util.ext.minute
import `in`.koreatech.koin.domain.util.ext.second
import `in`.koreatech.koin.domain.util.getTimerTickFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class GetBusTimerUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    var count = 1L

    operator fun invoke(
        departure: BusNode,
        arrival: BusNode
    ) = flow {
        val period = 1.second
        var time = System.currentTimeMillis()
        var list = getBusArrivalInfo(departure, arrival)

        while (true) {
            System.currentTimeMillis().let {
                if (it - time >= period) {

                    if (count / 60 > 0 || count == 0L) {
                        list = getBusArrivalInfo(departure, arrival)
                        count = 0
                    }

                    val nowLocalTime = LocalTime.now(ZoneId.of("Asia/Seoul"))

                    time += period
                    count++

                    val result = list.mapNotNull { busArrivalInfo ->
                        when (busArrivalInfo) {
                            is BusArrivalInfo.CityBusArrivalInfo -> busArrivalInfo.copy(
                                nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                },
                                nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                }
                            )
                            is BusArrivalInfo.ExpressBusArrivalInfo -> busArrivalInfo.copy(
                                nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                },
                                nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                }
                            )
                            is BusArrivalInfo.ShuttleBusArrivalInfo -> busArrivalInfo.copy(
                                nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                },
                                nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                    getDurationOrReset(
                                        it,
                                        busArrivalInfo.criteria,
                                        nowLocalTime
                                    )
                                }
                            )
                            is BusArrivalInfo.CommutingBusArrivalInfo -> null
                        }
                    }

                    emit(result)
                } else delay(period / 100)
            }
        }
    }

    private suspend fun getBusArrivalInfo(
        departure: BusNode,
        arrival: BusNode
    ): List<BusArrivalInfo> = mutableListOf<BusArrivalInfo>().apply {
        add(busRepository.getShuttleBusRemainTime(departure, arrival))
        if (departure != BusNode.Station && arrival != BusNode.Station) {
            add(busRepository.getExpressBusRemainTime(departure, arrival))
        }
        add(busRepository.getCityBusRemainTime(departure, arrival))
    }

    private fun getDurationOrReset(
        busRemainTime: Long,
        localTimeCriteria: LocalTime,
        localTimeNow: LocalTime
    ): Long {
        return (busRemainTime - Duration.between(localTimeCriteria, localTimeNow).seconds).let {
            if (it < 0) {
                count = 0L
                0
            } else {
                it
            }
        }
    }

}