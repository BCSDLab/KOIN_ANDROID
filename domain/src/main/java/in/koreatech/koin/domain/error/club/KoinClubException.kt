package `in`.koreatech.koin.domain.error.club

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinClubException : KoinErrorException() {
    class Unauthorized : KoinClubException()
    class AlreadyManager : KoinClubException()
    class DeletePermissionDenied : KoinClubException()
    class UserIdNotFound : KoinClubException()
    class KoinClubNotFound : KoinClubException()
    class NotKoinClubManager : KoinClubException()
    class QnaNotFound : KoinClubException()
    class AlreadyLiked : KoinClubException()
    class AlreadyNotLiked : KoinClubException()
}
