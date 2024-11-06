package `in`.koreatech.koin.domain.usecase.busv2

import `in`.koreatech.koin.domain.error.busv2.SearchBusError
import javax.inject.Inject

class SearchBusV2UseCase @Inject constructor(
   // private val busRepository
) {

    suspend operator fun invoke(departure: String, arrival: String): Result<Unit> {
        return when {
            departure.isEmpty() -> Result.failure(SearchBusError.EmptyDeparture())
            arrival.isEmpty() -> Result.failure(SearchBusError.EmptyArrival())
            else -> {
                // TODO repository Search bus
                // busRepository.searchBus(departure, arrival)
                Result.success(Unit)
            }
        }
    }
}