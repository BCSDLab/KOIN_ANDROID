package `in`.koreatech.koin.feature.callvan.ui.list.model

import `in`.koreatech.koin.domain.model.callvan.CallvanPostSearch

enum class CallvanItemState {
    DEFAULT,
    JOINED,
    CLOSED,
    OWNER_ACTIVE,
    OWNER_CLOSED
}

fun CallvanPostSearch.CallvanPost.toItemState(): CallvanItemState = when {
    isAuthor && status == "RECRUITING" -> CallvanItemState.OWNER_ACTIVE
    isAuthor && status == "CLOSED" -> CallvanItemState.OWNER_CLOSED
    !isAuthor && isJoined -> CallvanItemState.JOINED
    status == "RECRUITING" -> CallvanItemState.DEFAULT
    else -> CallvanItemState.CLOSED
}
