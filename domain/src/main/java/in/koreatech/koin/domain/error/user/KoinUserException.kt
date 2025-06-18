package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinUserException {
    // 400
    class LoginIdWrongFormatException : KoinErrorException()
    class PutUserRequestDataErrorException : KoinErrorException()
    class InvalidEmailException : KoinErrorException()
    class InvalidPhoneNumberException : KoinErrorException()
    class LoginIdNotMatchPhoneException : KoinErrorException()
    class LoginIdNotMatchEmailException : KoinErrorException()
    // 401
    class UnauthorizedException : KoinErrorException()
    // 404
    class LoginIdNotExistsException : KoinErrorException()
    class PutUserNotFoundException : KoinErrorException()
    class EmailNotFoundException : KoinErrorException()
    class PhoneNumberNotFoundException : KoinErrorException()
    // 409
    class PutUserNicknameOrEmailConflictException : KoinErrorException()
}
