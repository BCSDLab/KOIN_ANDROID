package `in`.koreatech.koin.domain.usecase.bus

import `in`.koreatech.koin.domain.constant.BUS_REMAIN_TIME_UPDATE_PERIOD
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.BusRemainTime
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import `in`.koreatech.koin.domain.util.BusTimeHandler
import `in`.koreatech.koin.domain.util.ext.minute
import `in`.koreatech.koin.domain.util.ext.second
import `in`.koreatech.koin.domain.util.getTimerFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class GetCityBusRemainTimeUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler,
    private val busTimeHandler: BusTimeHandler
) {
    private fun getBusFlow(
        departure: BusNode,
        arrival: BusNode
    ) = flow {
        while (true) {
            emit(busRepository.getCityBus(departure, arrival))
            delay(5.minute)
        }
    }


    operator fun invoke(
        departure: BusNode,
        arrival: BusNode
    ) = getTimerFlow().combine(
        getBusFlow(departure, arrival)
    ) { _: Unit, pair: Pair<BusRemainTime.CityBusRemainTime?, BusRemainTime.CityBusRemainTime?> ->
        "asdf"
    }
}