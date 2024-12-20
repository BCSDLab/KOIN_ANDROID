package `in`.koreatech.koin.domain.util

sealed interface Error

sealed interface DataError : Error {
    sealed interface Network {
        data class ServerErrorWithMessage(val message: String) : DataError
        data object RequestTimeout : DataError
        data object TooManyRequest : DataError
        data object NoInternet : DataError
        data object PayloadTooLarge : DataError
        data object ServerError : DataError
        data object Unknown : DataError
    }

    sealed interface Local {
        data object DiskFull : DataError
        data object Unknown : DataError
    }
}