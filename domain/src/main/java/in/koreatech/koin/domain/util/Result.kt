package `in`.koreatech.koin.domain.util

typealias RootError = Error

sealed interface Result<out DATA, out ERROR : RootError> {
    data class Success<out DATA, out ERROR : RootError>(val data: DATA) : Result<DATA, ERROR>
    data class Error<out DATA, out ERROR : RootError>(val error: ERROR) : Result<DATA, ERROR>
}
