package `in`.koreatech.koin.domain.error.club

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to club APIs.
 * Don't add Club prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */
sealed class KoinClubException : KoinErrorException() {
    /*
     * Exceptions for 400 Bad Request
     */
    class AlreadyManagerException : KoinClubException()
    class WrongInputDataException : KoinClubException()

    /*
     * Exceptions for 401 Unauthorized
     */
    class UnauthorizedException : KoinClubException()

    /*
     * Exceptions for 403 Forbidden
     */
    class NotClubManagerException : KoinClubException()
    class NotAllowedUserException : KoinClubException()
    class DeletePermissionDeniedException : KoinClubException()

    /*
     * Exceptions for 404 Not Found
     */
    class LoginIdNotFoundException : KoinClubException()
    class ClubNotFoundException : KoinClubException()
    class ClubCategoryNotFoundException : KoinClubException()
    class QnaNotFoundException : KoinClubException()
    class AlreadyNotLikedException : KoinClubException()
    class ClubRecruitNotFoundException : KoinClubException()
    class ClubEventNotFoundException : KoinClubException()

    /*
     * Exceptions for 409 Conflict
     */
    class AlreadyLikedException : KoinClubException()
    class AlreadyRecruitingException : KoinClubException()
}
