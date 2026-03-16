package `in`.koreatech.koin.feature.callvan.enums

enum class ConfirmType {
    JOIN,
    CANCEL_JOIN,
    CLOSE,
    REOPEN;

    companion object {
        fun from(state: CallvanRouteState): ConfirmType? = when (state) {
            CallvanRouteState.DEFAULT -> JOIN
            CallvanRouteState.JOINED -> CANCEL_JOIN
            CallvanRouteState.OWNER_ACTIVE -> CLOSE
            CallvanRouteState.OWNER_CLOSED -> REOPEN
            CallvanRouteState.CLOSED -> null
        }
    }
}
