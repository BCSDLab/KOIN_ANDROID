package `in`.koreatech.koin.data.util

import com.google.gson.Gson
import `in`.koreatech.koin.data.response.ErrorResponse
import `in`.koreatech.koin.domain.error.KoinErrorException
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import retrofit2.HttpException

fun HttpException.getErrorResponse(): ErrorResponse {
    return Gson().fromJson(response()?.errorBody()?.string(), ErrorResponse::class.java)
}

fun ErrorResponse.toKoinUnknownErrorException(): KoinUnknownErrorException {
    return KoinUnknownErrorException(this.code, this.message, this.errorTraceId)
}

fun <T> Result<T>.mapHttpFailure(
    e400: KoinErrorException? = null,
    e401: KoinErrorException? = null,
    e403: KoinErrorException? = null,
    e404: KoinErrorException? = null,
    e409: KoinErrorException? = null,
    e429: KoinErrorException? = null,
    e500: KoinErrorException? = null
): Result<T> {
    val exception = exceptionOrNull() ?: return this
    if (exception is HttpException) {
        val default = exception.getErrorResponse().toKoinUnknownErrorException()
        val mapped = when (exception.code()) {
            400 -> e400
            401 -> e401
            403 -> e403
            404 -> e404
            409 -> e409
            429 -> e429
            in 500..599 -> e500
            else -> null
        } ?: default

        return Result.failure(mapped)
    }
    return this
}
