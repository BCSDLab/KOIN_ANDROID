package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to user APIs.
 * Don't add User prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */
sealed class KoinUserException {
    /*
     * Exceptions for 400 Bad Request
     * format: {data type}InvalidException
     * or {data type}NotMatch{other data type}Exception
     */
    class DataInvalidException : KoinErrorException()
    class LoginIdInvalidException : KoinErrorException()
    class EmailInvalidException : KoinErrorException()
    class PhoneNumberInvalidException : KoinErrorException()
    class LoginIdNotMatchPhoneException : KoinErrorException()
    class LoginIdNotMatchEmailException : KoinErrorException()

    /*
     * Exceptions for 401 Unauthorized
     */
    class UnauthorizedException : KoinErrorException()

    /*
     * Exceptions for 403 Forbidden
     * format: {data type}NotFoundException
     */
    class UserNotFoundException : KoinErrorException()
    class LoginIdNotFoundException : KoinErrorException()
    class EmailNotFoundException : KoinErrorException()
    class PhoneNumberNotFoundException : KoinErrorException()

    /*
     * Exceptions for 409 Conflict
     */
    class NicknameOrEmailConflictException : KoinErrorException()
}
