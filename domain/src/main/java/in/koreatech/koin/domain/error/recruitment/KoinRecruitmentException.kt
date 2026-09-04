package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinRecruitmentException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class InvalidArgumentException(override var message: String? = null) : KoinRecruitmentException()
    class InvalidRequestBodyException(override var message: String? = null) : KoinRecruitmentException()
    class NotReadableHttpMessageException(override var message: String? = null) : KoinRecruitmentException()
    class ActivityEndDateRequiredException(override var message: String? = null) : KoinRecruitmentException()
    class ActivityEndDateMustBeNullException(override var message: String? = null) : KoinRecruitmentException()
    class InvalidStartDateAfterEndDateException(override var message: String? = null) : KoinRecruitmentException()
    class InvalidDeadlineDateException(override var message: String? = null) : KoinRecruitmentException()
    class InvalidRoleCompositionException(override var message: String? = null) : KoinRecruitmentException()
    class InvalidRequestException : KoinRecruitmentException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedUserException(override var message: String? = null) : KoinRecruitmentException()
    class UnauthorizedException : KoinRecruitmentException()

    /*
     * Exceptions for 403
     */
    class ForbiddenException(override var message: String? = null) : KoinRecruitmentException()
    class ForbiddenUserTypeException(override var message: String? = null) : KoinRecruitmentException()
    class RecruitmentForbiddenException(override var message: String? = null) : KoinRecruitmentException()

    /*
     * Exceptions for 404
     */
    class NotFoundException(override var message: String? = null) : KoinRecruitmentException()
    class RoleNotFoundException : KoinRecruitmentException()
    class NotFoundUserException(override var message: String? = null) : KoinRecruitmentException()
    class ProfileNotFoundException(override var message: String? = null) : KoinRecruitmentException()
    class RecruitmentNotFoundException(override var message: String? = null) : KoinRecruitmentException()

    /*
     * Exceptions for 409
     */
    class RecruitmentClosedException(override var message: String? = null) : KoinRecruitmentException()
    class RoleUpdateNotAllowedException : KoinRecruitmentException()
    class MaxParticipantsBelowAcceptedException : KoinRecruitmentException()
    class RecruitmentTypeChangeNotAllowedException : KoinRecruitmentException()
    class ProfileRequiredException(override var message: String? = null) : KoinRecruitmentException()
    class RecruitmentRoleClosedException(override var message: String? = null) : KoinRecruitmentException()
    class CapacityFullException(override var message: String? = null) : KoinRecruitmentException()
    class ApplicationDuplicateException(override var message: String? = null) : KoinRecruitmentException()
    class RequestTooFastException(override var message: String? = null) : KoinRecruitmentException()
}
