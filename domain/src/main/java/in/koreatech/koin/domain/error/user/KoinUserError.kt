package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinUserError {
    data object LoginIdErrorExists : KoinErrorException()
    data object LoginIdNotExists : KoinErrorException()
    data object LoginIdWrongFormat : KoinErrorException()
    data object LoginIdNotMatchEmail : KoinErrorException()
    data object LoginIdNotMatchPhone : KoinErrorException()
    data object RequestDataInvalid : KoinErrorException()
    data object Unauthorized : KoinErrorException()
}