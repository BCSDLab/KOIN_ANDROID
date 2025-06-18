package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinUserException {
    class LoginIdNotExistsException : KoinErrorException()
    class LoginIdWrongFormatException : KoinErrorException()
    class LoginIdNotMatchEmailException : KoinErrorException()
    class LoginIdNotMatchPhoneException : KoinErrorException()
    class PutUserRequestDataErrorException : KoinErrorException()
    class PutUserNotFoundException : KoinErrorException()
    class PutUserNicknameOrEmailConflictException : KoinErrorException()
    class InvalidEmailException : KoinErrorException()
    class EmailNotFoundException : KoinErrorException()
    class InvalidPhoneNumberException : KoinErrorException()
    class PhoneNumberNotFoundException : KoinErrorException()
    class UnauthorizedException : KoinErrorException()
}
