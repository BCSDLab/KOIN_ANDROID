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
     * Exceptions for 400
     */
    class InvalidRequestException : KoinRecruitmentException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedException : KoinRecruitmentException()

    /*
     * Exceptions for 403
     */
    class ForbiddenException : KoinRecruitmentException()
    class ForbiddenUserTypeException : KoinRecruitmentException()

    /*
     * Exceptions for 404
     */
    class NotFoundException : KoinRecruitmentException()
    class RoleNotFoundException : KoinRecruitmentException()

    /*
     * Exceptions for 400 (updateRecruitment)
     */
    class InvalidDeadlineDateException : KoinRecruitmentException()
    class InvalidRoleCompositionException : KoinRecruitmentException()
    class InvalidStartDateAfterEndDateException : KoinRecruitmentException()
    class InvalidRequestBodyException : KoinRecruitmentException()

    /*
     * Exceptions for 409
     */
    class RecruitmentClosedException : KoinRecruitmentException()
    class RoleUpdateNotAllowedException : KoinRecruitmentException()
    class MaxParticipantsBelowAcceptedException : KoinRecruitmentException()
    class RecruitmentTypeChangeNotAllowedException : KoinRecruitmentException()
}
