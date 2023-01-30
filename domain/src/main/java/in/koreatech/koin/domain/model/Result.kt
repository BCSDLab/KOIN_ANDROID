package `in`.koreatech.koin.domain.model

import `in`.koreatech.koin.domain.model.error.ErrorType

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(
        val errorMessage: String
    ) : Result<Nothing>()

    val isSuccess get() = this is Success<*>
    val isError get() = this is Error

    inline fun onSuccess(block: (data: T) -> Unit): Result<T> {
        if (this is Success<T>) block(data)
        return this
    }

    inline fun onFailure(block: (errorMessage: String) -> Unit): Result<T> {
        if (this is Error) block(errorMessage)
        return this
    }
}

fun <T : Any> T.toResult(): Result<T> = Result.Success(this)