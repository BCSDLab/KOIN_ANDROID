package `in`.koreatech.koin.domain.error.club

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class ClubError : KoinErrorException() {
    data class Unauthorized(override val message: String?) : ClubError()
    data class AlreadyManager(override val message: String?) : ClubError()
    data class DeletePermissionDenied(override val message: String?) : ClubError()
    data class Forbidden(override val message: String?) : ClubError()
    data class UserIdOrClubNotFound(override val message: String?) : ClubError()
    data class ClubNotFound(override val message: String?) : ClubError()
    data class NotClubManager(override val message: String?) : ClubError()
    data class QnaNotFound(override val message: String?) : ClubError()
    data class AlreadyLiked(override val message: String?) : ClubError()
    data class AlreadyNotLiked(override val message: String?) : ClubError()
}
