package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinRecruitmentException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class InvalidArgumentException(override val message: String? = null) : KoinRecruitmentException()
    class InvalidRequestBodyException(override val message: String? = null) : KoinRecruitmentException()
    class NotReadableHttpMessageException(override val message: String? = null) : KoinRecruitmentException()
    class ActivityEndDateRequiredException(override val message: String? = null) : KoinRecruitmentException()
    class ActivityEndDateMustBeNullException(override val message: String? = null) : KoinRecruitmentException()
    class InvalidStartDateAfterEndDateException(override val message: String? = null) : KoinRecruitmentException()
    class InvalidDeadlineDateException(override val message: String? = null) : KoinRecruitmentException()
    class InvalidRoleCompositionException(override val message: String? = null) : KoinRecruitmentException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedUserException(override val message: String? = null) : KoinRecruitmentException()

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
    class ForbiddenException(override val message: String? = null) : KoinRecruitmentException()
    class ForbiddenUserTypeException(override val message: String? = null) : KoinRecruitmentException()
    class RecruitmentForbiddenException(override val message: String? = null) : KoinRecruitmentException()

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
    class NotFoundException(override val message: String? = null) : KoinRecruitmentException()
    class NotFoundUserException(override val message: String? = null) : KoinRecruitmentException()
    class ProfileNotFoundException(override val message: String? = null) : KoinRecruitmentException()
    class RecruitmentNotFoundException(override val message: String? = null) : KoinRecruitmentException()

    /*
     * Exceptions for 409
     */
    class ProfileRequiredException(override val message: String? = null) : KoinRecruitmentException()
    class RecruitmentClosedException(override val message: String? = null) : KoinRecruitmentException()
    class RecruitmentRoleClosedException(override val message: String? = null) : KoinRecruitmentException()
    class CapacityFullException(override val message: String? = null) : KoinRecruitmentException()
    class ApplicationDuplicateException(override val message: String? = null) : KoinRecruitmentException()
    class RequestTooFastException(override val message: String? = null) : KoinRecruitmentException()
}
