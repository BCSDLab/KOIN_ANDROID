package `in`.koreatech.koin.domain.util

sealed interface Error

sealed interface DataError : Error {
    sealed interface Network {
        data class ServerErrorWithMessage(val message: String) : DataError, Network
        data object RequestTimeout : DataError, Network
        data object TooManyRequest : DataError, Network
        data object NoInternet : DataError, Network
        data object PayloadTooLarge : DataError, Network
        data object ServerError : DataError, Network
        data object Unknown : DataError, Network
    }

    sealed interface Local {
        data object DiskFull : DataError, Local
        data object Unknown : DataError, Local
    }
}