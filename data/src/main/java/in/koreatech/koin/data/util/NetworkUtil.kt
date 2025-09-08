package `in`.koreatech.koin.data.util

import com.google.gson.Gson
import `in`.koreatech.koin.data.response.ErrorResponse
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.error.network.KoinNetworkException
import retrofit2.HttpException

fun HttpException.getErrorResponse(): ErrorResponse {
    return Gson().fromJson(response()?.errorBody()?.string(), ErrorResponse::class.java)
}

fun ErrorResponse.toKoinUnknownErrorException(): KoinUnknownErrorException {
    return KoinUnknownErrorException(this.code, this.message, this.errorTraceId)
}

inline fun <T> runCatchingWithNetwork(
    block: () -> T
): Result<T> {
    return runCatching(block).onFailure { exception ->
        return Result.failure(
            if (exception.cause is KoinNetworkException.NetworkUnavailableException) {
                KoinNetworkException.NetworkUnavailableException()
            } else {
                exception
            }
        )
    }
}
