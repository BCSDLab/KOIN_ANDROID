package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinUserError {
    class LoginIdErrorExists : KoinErrorException()
    class LoginIdNotExists : KoinErrorException()
    class LoginIdWrongFormat : KoinErrorException()
    class LoginIdNotMatchEmail : KoinErrorException()
    class LoginIdNotMatchPhone : KoinErrorException()
    class RequestDataInvalid : KoinErrorException()
    class Unauthorized : KoinErrorException()
    class PutUserRequestDataError : KoinErrorException()
    class PutUserPhoneNumberNotAuthorized : KoinErrorException()
    class PutUserNotFound : KoinErrorException()
    class PutUserNicknameOrEmailConflict : KoinErrorException()
    class InvalidEmailException : KoinErrorException()
    class EmailNotFoundException : KoinErrorException()
    class InvalidPhoneNumberException : KoinErrorException()
    class PhoneNumberNotFoundException : KoinErrorException()
    class UserUnauthorizedException : KoinErrorException()
}
