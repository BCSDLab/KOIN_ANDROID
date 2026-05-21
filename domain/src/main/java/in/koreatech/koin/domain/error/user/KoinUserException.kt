package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to user APIs.
 * Don't add User prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */
sealed class KoinUserException : KoinErrorException() {
    /*
     * Exceptions for 400 Bad Request
     * format: {data type}InvalidException
     * or {data type}NotMatch{other data type}Exception
     */
    class DataInvalidException : KoinUserException()
    class LoginIdInvalidException : KoinUserException()
    class EmailInvalidException : KoinUserException()
    class PhoneNumberInvalidException : KoinUserException()
    class NicknameInvalidException : KoinUserException()
    class VerificationCodeInvalidException : KoinUserException()
    class LoginIdNotMatchPhoneException : KoinUserException()
    class LoginIdNotMatchEmailException : KoinUserException()
    class LoginCredentialInvalidException : KoinUserException()

    /*
     * Exceptions for 401 Unauthorized
     */
    class UnauthorizedException : KoinUserException()

    /*
     * Exceptions for 404 Not Found
     */
    class UserNotFoundException : KoinUserException()
    class LoginIdNotFoundException : KoinUserException()
    class EmailNotFoundException : KoinUserException()
    class PhoneNumberNotFoundException : KoinUserException()
    class VerificationCodeExpiredException : KoinUserException()

    /*
     * Exceptions for 409 Conflict
     */
    class PhoneNumberConflictException : KoinUserException()
    class EmailConflictException : KoinUserException()
    class LoginIdConflictException : KoinUserException()
    class NicknameConflictException : KoinUserException()
    class NicknameOrEmailConflictException : KoinUserException()

    /*
     * Exceptions for 429 Too Many Requests
     */
    class VerificationCodeRequestCountExceededException : KoinUserException()
}
