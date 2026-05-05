package `in`.koreatech.koin.domain.error.callvan

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinCallvanException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class InvalidRequestBodyException : KoinCallvanException()
    class InvalidCustomLocationNameException : KoinCallvanException()
    class CallvanReportSelfException : KoinCallvanException()
    class CallvanPostNotRecruitingException : KoinCallvanException()
    class CallvanPostFullException : KoinCallvanException()
    class CallvanPostAuthorException : KoinCallvanException()
    class CallvanPostReopenFailedFullException : KoinCallvanException()
    class CallvanPostReopenFailedTimeException : KoinCallvanException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedUserException : KoinCallvanException()

    /*
     * Exceptions for 403
     */
    class ForbiddenParticipantException : KoinCallvanException()
    class ForbiddenAuthorException : KoinCallvanException()
    class CallvanReportOnlyParticipantException : KoinCallvanException()
    class CallvanRestrictedUserException : KoinCallvanException()

    /*
     * Exceptions for 404
     */
    class NotFoundUserException : KoinCallvanException()
    class NotFoundArticleException : KoinCallvanException()

    /*
     * Exceptions for 409
     */
    class CallvanReportAlreadyPendingException : KoinCallvanException()
    class CallvanAlreadyJoinedException : KoinCallvanException()
}
