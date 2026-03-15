package `in`.koreatech.koin.feature.callvan.ui.list.model

enum class CallvanConfirmType {
    JOIN,
    CANCEL_JOIN,
    CLOSE,
    REOPEN;

    companion object {
        fun from(state: CallvanItemState): CallvanConfirmType? = when (state) {
            CallvanItemState.DEFAULT -> JOIN
            CallvanItemState.JOINED -> CANCEL_JOIN
            CallvanItemState.OWNER_ACTIVE -> CLOSE
            CallvanItemState.OWNER_CLOSED -> REOPEN
            CallvanItemState.CLOSED -> null
        }
    }
}