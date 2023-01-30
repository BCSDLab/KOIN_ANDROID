package `in`.koreatech.koin.domain.usecase.bus.search

import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.toResult
import `in`.koreatech.koin.domain.repository.BusRepository
import java.time.LocalDateTime
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result

class SearchBusUseCase @Inject constructor(
    private val busRepository: BusRepository,
    private val busErrorHandler: BusErrorHandler
) {
    suspend operator fun invoke(
        dateTime: LocalDateTime,
        departure: BusNode,
        arrival: BusNode
    ): Result<List<BusSearchResult>> {
        return try {
            busRepository.searchBus(dateTime, departure, arrival).toResult()
        } catch (t: Throwable) {
            busErrorHandler.handleSearchBusError(t)
        }
    }
}