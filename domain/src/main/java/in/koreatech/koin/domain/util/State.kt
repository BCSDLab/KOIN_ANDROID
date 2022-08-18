package `in`.koreatech.koin.domain.util

sealed class State<T> {
    class Loading<T> : State<T>()
    class Success<T>(val data: T): State<T>()
    class Failure<T>(val t: Throwable): State<T>()
}
