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

fun <T> Result<T>.mapHttpFailure(
    block: HttpExceptionMapper.() -> Unit
): Result<T> {
    val exception = exceptionOrNull() ?: return this
    if (exception !is HttpException) return this
    val mapper = HttpExceptionMapper(exception)
    mapper.block()

    return Result.failure(mapper.map())
}

class HttpExceptionMapper(private val exception: HttpException) {
    val exceptions = mutableMapOf<Pair<Int, String?>, KoinErrorException>()

    inner class OnBuilder(val statusCode: Int, val errorCode: String? = null) {
        infix fun throws(exception: KoinErrorException) {
            exceptions[statusCode to errorCode] = exception
        }
    }

    inner class OnRangeBuilder(val statusCodes: IntRange, val errorCode: String? = null) {
        infix fun throws(exception: KoinErrorException) {
            for (statusCode in statusCodes) {
                exceptions[statusCode to errorCode] = exception
            }
        }
    }

    fun on(statusCode: Int, errorCode: String? = null) = OnBuilder(statusCode, errorCode)

    fun on(statusCodes: IntRange, errorCode: String? = null) = OnRangeBuilder(statusCodes, errorCode)

    internal fun map(): KoinErrorException = with(exception) {
        val errorResponse = getErrorResponse()
        return exceptions[code() to errorResponse.code] ?: exceptions[code() to null] ?: errorResponse.toKoinUnknownErrorException()
    }
}
