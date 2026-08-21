package `in`.koreatech.koin.data.stomp

sealed interface KoinStompConnectionState {
    data object Connecting : KoinStompConnectionState

    data object Connected : KoinStompConnectionState

    data class Reconnecting(val attempt: Int) : KoinStompConnectionState

    data object Disconnected : KoinStompConnectionState
}
