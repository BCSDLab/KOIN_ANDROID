package `in`.koreatech.koin.domain.usecase.bus.search

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SearchBusUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(
        dateTime: LocalDateTime,
        departure: BusNode,
        arrival: BusNode
    ): Pair<List<BusSearchResult>?, ErrorHandler?> {
        return try {
            busRepository.searchBus(dateTime, departure, arrival) to null
        } catch (t: Throwable) {
            null to busErrorHandler.handleSearchBusError(t)
        }
    }
}