package `in`.koreatech.koin.domain.state.network

sealed class NetworkStatus {
    data object Connected : NetworkStatus()
    data object Disconnected : NetworkStatus()
}
