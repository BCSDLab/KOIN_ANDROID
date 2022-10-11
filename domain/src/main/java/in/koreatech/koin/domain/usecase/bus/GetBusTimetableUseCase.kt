package `in`.koreatech.koin.domain.usecase.bus

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import javax.inject.Inject

class GetBusTimetableUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {

}