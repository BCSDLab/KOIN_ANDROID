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
    return KoinUnknownErrorException(this.code, this.message, this.errorTraceId).apply {
        message = this@toKoinUnknownErrorException.message
    }
}

@Deprecated(
    message = "Use the Kotlin DSL version of mapHttpFailure instead.",
    replaceWith = ReplaceWith("mapHttpFailure { /* mapping */ }")
)
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

/**
 * HttpException을 KoinErrorException으로 매핑하는 Result 확장 함수
 *
 * ```kotlin
 * mapHttpFailure {
 *     on(400, "ERROR_CODE") throws KoinException.AnotherException() // HTTP 상태 코드 및 에러 코드로 분기 처리
 *     on(400) throws KoinException.Exception() // HTTP 상태 코드만으로 분기 처리, 에러 코드 없음
 *     on(500..599, "ERROR_CODE") throws KoinException.AnotherServerException() // HTTP 상태 코드 범위 및 에러 코드 분기 처리
 *     on(500..599) throws KoinException.ServerException() // HTTP 상태 코드 범위로 분기 처리
 * }
 * ```
 *
 * 모든 `on` 함수 중 가장 마지막 `on` 함수의 우선순위가 제일 높다.
 *
 * @param block 매핑 로직
 * @return 매핑된 Result
 */
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
    val errorResponse: ErrorResponse by lazy { exception.getErrorResponse() }
    var mappedException: KoinErrorException? = null

    inner class OnBuilder(val matched: Boolean) {
        infix fun throws(exception: KoinErrorException) {
            if (matched) mappedException = exception
        }
    }

    fun on(statusCode: Int) = OnBuilder(exception.code() == statusCode)

    fun on(statusCodes: IntRange) = OnBuilder(exception.code() in statusCodes)

    fun on(statusCode: Int, errorCode: String) = OnBuilder(exception.code() == statusCode && errorCode == errorResponse.code)

    fun on(statusCodes: IntRange, errorCode: String) = OnBuilder(exception.code() in statusCodes && errorCode == errorResponse.code)

    internal fun map(): KoinErrorException {
        val exception = mappedException ?: return errorResponse.toKoinUnknownErrorException()
        exception.message = errorResponse.message
        return exception
    }
}
