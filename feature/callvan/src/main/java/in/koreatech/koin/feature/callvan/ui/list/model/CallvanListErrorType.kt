package `in`.koreatech.koin.feature.callvan.ui.list.model

import `in`.koreatech.koin.domain.error.callvan.KoinCallvanException

enum class CallvanListErrorType {
    POST_NOT_RECRUITING,
    POST_FULL,
    ALREADY_JOINED,
    NOT_FOUND_ARTICLE,
    FORBIDDEN_AUTHOR,
    FORBIDDEN_PARTICIPANT,
    POST_AUTHOR_CANNOT_LEAVE,
    REOPEN_FAILED_FULL,
    REOPEN_FAILED_TIME,
    UNKNOWN
}

fun Throwable.toListErrorType(): CallvanListErrorType = when (this) {
    is KoinCallvanException.CallvanPostNotRecruitingException -> CallvanListErrorType.POST_NOT_RECRUITING
    is KoinCallvanException.CallvanPostFullException -> CallvanListErrorType.POST_FULL
    is KoinCallvanException.CallvanAlreadyJoinedException -> CallvanListErrorType.ALREADY_JOINED
    is KoinCallvanException.NotFoundArticleException -> CallvanListErrorType.NOT_FOUND_ARTICLE
    is KoinCallvanException.ForbiddenAuthorException -> CallvanListErrorType.FORBIDDEN_AUTHOR
    is KoinCallvanException.ForbiddenParticipantException -> CallvanListErrorType.FORBIDDEN_PARTICIPANT
    is KoinCallvanException.CallvanPostAuthorException -> CallvanListErrorType.POST_AUTHOR_CANNOT_LEAVE
    is KoinCallvanException.CallvanPostReopenFailedFullException -> CallvanListErrorType.REOPEN_FAILED_FULL
    is KoinCallvanException.CallvanPostReopenFailedTimeException -> CallvanListErrorType.REOPEN_FAILED_TIME
    else -> CallvanListErrorType.UNKNOWN
}
