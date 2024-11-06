package `in`.koreatech.koin.domain.error.busv2

sealed class SearchBusError: Throwable() {
    class EmptyDeparture: SearchBusError()
    class EmptyArrival: SearchBusError()
}