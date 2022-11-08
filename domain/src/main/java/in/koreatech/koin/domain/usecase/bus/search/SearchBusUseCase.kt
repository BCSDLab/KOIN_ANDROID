package `in`.koreatech.koin.domain.usecase.bus.search

import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SearchBusUseCase @Inject constructor(
    private val busRepository: BusRepository
) {
    suspend operator fun invoke(
        dateTime: LocalDateTime,
        departure: BusNode,
        arrival: BusNode
    ) = busRepository.searchBus(dateTime, departure, arrival)
}