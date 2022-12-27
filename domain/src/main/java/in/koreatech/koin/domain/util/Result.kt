package `in`.koreatech.koin.domain.util

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(
        val t: Throwable,
        val errorMessage: String,
    ) : Result<Nothing>()

    val isSuccess get() = this is Success<T>
    val isFailure get() = !isSuccess

    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        (this as? Success<T>)?.data?.let { block(it) }
        return this
    }

    inline fun onFailure(block: (Throwable, String) -> Unit): Result<T> {
        (this as? Failure)?.let { block(it.t, it.errorMessage) }
        return this
    }
}