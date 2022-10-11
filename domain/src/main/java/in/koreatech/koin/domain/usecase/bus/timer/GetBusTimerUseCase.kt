package `in`.koreatech.koin.domain.usecase.bus.timer

import `in`.koreatech.koin.domain.constant.BUS_REMAIN_TIME_UPDATE_PERIOD
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.BusRemainTime
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.util.getTimerTickFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetBusTimerUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    private fun getBusDataFlow(
        departure: BusNode,
        arrival: BusNode
    ) = flow<List<BusArrivalInfo<BusRemainTime>>> {
        var isFirst = false
        while (true) {
            val list = mutableListOf<BusArrivalInfo<BusRemainTime>>()
            val arriveTime = busRepository.searchBus(LocalDateTime.now(), departure, arrival)

            list.add(busRepository.getShuttleBusRemainTime(departure, arrival, isFirst).let {
                BusArrivalInfo(
                    nowBus = it.first,
                    nextBus = it.second,
                    nowBusTime = arriveTime.find { it.busType == BusType.Shuttle }?.busTime?.toLocalTime()
                )
            })

            list.add(busRepository.getCommutingBusRemainTime(departure, arrival, isFirst).let {
                BusArrivalInfo(
                    nowBus = it.first,
                    nextBus = it.second,
                    nowBusTime = arriveTime.find { it.busType == BusType.Shuttle }?.busTime?.toLocalTime()
                )
            })

            if (departure != BusNode.Station && arrival != BusNode.Station) {
                list.add(busRepository.getExpressBusRemainTime(departure, arrival, isFirst).let {
                    BusArrivalInfo(
                        nowBus = it.first,
                        nextBus = it.second,
                        nowBusTime = arriveTime.find { it.busType == BusType.Shuttle }?.busTime?.toLocalTime()
                    )
                })
            }

            list.add(busRepository.getCityBusRemainTime(departure, arrival, isFirst).let {
                BusArrivalInfo(
                    nowBus = it.first,
                    nextBus = it.second,
                    nowBusTime = arriveTime.find { it.busType == BusType.Shuttle }?.busTime?.toLocalTime()
                )
            })

            emit(list)

            delay(BUS_REMAIN_TIME_UPDATE_PERIOD)
            isFirst = true
        }
    }

    operator fun invoke(
        departure: BusNode,
        arrival: BusNode
    ) = getBusDataFlow(
        departure,
        arrival
    ).combine(getTimerTickFlow {}) { list: List<BusArrivalInfo<BusRemainTime>>, _: Unit ->
        ArrayList(list)
    }
}