package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R

enum class CallvanConfirmType(@StringRes val titleRes: Int) {
    JOIN(R.string.callvan_confirm_join_title),
    CANCEL_JOIN(R.string.callvan_confirm_cancel_title),
    CLOSE(R.string.callvan_confirm_close_title),
    REOPEN(R.string.callvan_confirm_reopen_title);

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
