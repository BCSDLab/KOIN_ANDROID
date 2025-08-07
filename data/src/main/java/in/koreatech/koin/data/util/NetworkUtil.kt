package `in`.koreatech.koin.data.util

import com.google.gson.Gson
import `in`.koreatech.koin.data.response.ErrorResponse
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.error.network.KoinNetworkException
import `in`.koreatech.koin.domain.service.NetworkConnectivityService
import retrofit2.HttpException

fun HttpException.getErrorResponse(): ErrorResponse {
    return Gson().fromJson(response()?.errorBody()?.string(), ErrorResponse::class.java)
}

fun ErrorResponse.toKoinUnknownErrorException(): KoinUnknownErrorException {
    return KoinUnknownErrorException(this.code, this.message, this.errorTraceId)
}

inline fun <T> runCatchingWithNetwork(
    networkConnectivityService: NetworkConnectivityService,
    block: () -> T
): Result<T> {
    return if (networkConnectivityService.isConnected()) {
        runCatching(block)
    } else {
        Result.failure(KoinNetworkException.NetworkUnavailableException())
    }
}
