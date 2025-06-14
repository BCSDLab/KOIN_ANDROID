package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

class PutUserRequestDataError : KoinErrorException()
class PutUserPhoneNumberNotAuthorized : KoinErrorException()
class PutUserNotFound : KoinErrorException()
class PutUserNicknameOrEmailConflict : KoinErrorException()
class InvalidEmailException : KoinErrorException()
class EmailNotFoundException : KoinErrorException()
class InvalidPhoneNumberException : KoinErrorException()
class PhoneNumberNotFoundException : KoinErrorException()