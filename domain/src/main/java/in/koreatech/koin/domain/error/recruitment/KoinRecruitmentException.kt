package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinRecruitmentException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class InvalidArgumentException : KoinRecruitmentException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedUserException : KoinRecruitmentException()

    /*
     * Exceptions for 403
     */
    class ForbiddenException : KoinRecruitmentException()

    /*
     * Exceptions for 404
     */
    class NotFoundException : KoinRecruitmentException()
}
