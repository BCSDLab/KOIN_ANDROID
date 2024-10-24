package `in`.koreatech.koin.domain.usecase.bus.timer

import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.util.ext.second
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class GetBusTimerUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    operator fun invoke(
        departure: BusNode,
        arrival: BusNode
    ) = flow {
        var count = 1L
        val period = 1.second
        var time = System.currentTimeMillis() - 1.second
        var list = getBusArrivalInfo(departure, arrival)

        if (departure != arrival) {
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
                                is BusArrivalInfo.CityBusArrivalInfo -> BusArrivalInfo.CityBusArrivalInfo(
                                    departure = departure,
                                    arrival = arrival,
                                    nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                        getDurationOrReset(
                                            it,
                                            busArrivalInfo.criteria,
                                            nowLocalTime
                                        ) { count = 0L }
                                    },
                                    nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                        getDurationOrReset(
                                            it,
                                            busArrivalInfo.criteria,
                                            nowLocalTime
                                        ) { count = 0L }
                                    },
                                    nowBusArrivalTime = busArrivalInfo.nowBusArrivalTime,
                                    nextBusArrivalTime = busArrivalInfo.nextBusArrivalTime,
                                    criteria = busArrivalInfo.criteria,
                                    busNumber = busArrivalInfo.busNumber
                                )
                                is BusArrivalInfo.ExpressBusArrivalInfo ->
                                    BusArrivalInfo.ExpressBusArrivalInfo(
                                        departure = departure,
                                        arrival = arrival,
                                        nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                            getDurationOrReset(
                                                it,
                                                busArrivalInfo.criteria,
                                                nowLocalTime
                                            ) { count = 0L }
                                        },
                                        nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                            getDurationOrReset(
                                                it,
                                                busArrivalInfo.criteria,
                                                nowLocalTime
                                            ) { count = 0L }
                                        },
                                        nowBusArrivalTime = busArrivalInfo.nowBusArrivalTime,
                                        nextBusArrivalTime = busArrivalInfo.nextBusArrivalTime,
                                        criteria = busArrivalInfo.criteria
                                    )

                                is BusArrivalInfo.ShuttleBusArrivalInfo ->
                                    BusArrivalInfo.ShuttleBusArrivalInfo(
                                        departure = departure,
                                        arrival = arrival,
                                        nowBusRemainTime = busArrivalInfo.nowBusRemainTime?.let {
                                            getDurationOrReset(
                                                it,
                                                busArrivalInfo.criteria,
                                                nowLocalTime
                                            ) { count = 0L }
                                        },
                                        nextBusRemainTime = busArrivalInfo.nextBusRemainTime?.let {
                                            getDurationOrReset(
                                                it,
                                                busArrivalInfo.criteria,
                                                nowLocalTime
                                            ) { count = 0L }
                                        },
                                        nowBusArrivalTime = busArrivalInfo.nowBusArrivalTime,
                                        nextBusArrivalTime = busArrivalInfo.nextBusArrivalTime,
                                        criteria = busArrivalInfo.criteria
                                    )
                                is BusArrivalInfo.CommutingBusArrivalInfo -> null
                            }
                        }

                        emit(result)
                    } else delay(period / 100)
                }
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

    private inline fun getDurationOrReset(
        busRemainTime: Long,
        localTimeCriteria: LocalTime,
        localTimeNow: LocalTime,
        onTimeEnd: () -> Unit
    ): Long {
        return (busRemainTime - Duration.between(localTimeCriteria, localTimeNow).seconds).let {
            if (it < 0) {
                onTimeEnd()
                0
            } else {
                it
            }
        }
    }

}