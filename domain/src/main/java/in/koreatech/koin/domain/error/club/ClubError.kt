package `in`.koreatech.koin.domain.error.club

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class ClubError {
    data object BadRequest : KoinErrorException()
    data object Unauthorized : KoinErrorException()
    data object Forbidden : KoinErrorException()
    data object NotFound : KoinErrorException()
    data object NotFoundUserId : KoinErrorException()
}