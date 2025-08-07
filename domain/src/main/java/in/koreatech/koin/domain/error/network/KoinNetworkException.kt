package `in`.koreatech.koin.domain.error.network

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinNetworkException : KoinErrorException() {
    class NetworkUnavailableException : KoinNetworkException()
}
