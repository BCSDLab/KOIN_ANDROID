package `in`.koreatech.koin.domain.error.club

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class ClubError : KoinErrorException() {
    object Unauthorized : ClubError()
    object AlreadyManager : ClubError()
    object DeletePermissionDenied : ClubError()
    object UserIdNotFound : ClubError()
    object ClubNotFound : ClubError()
    object NotClubManager : ClubError()
    object QnaNotFound : ClubError()
    object AlreadyLiked : ClubError()
    object AlreadyNotLiked : ClubError()
}
