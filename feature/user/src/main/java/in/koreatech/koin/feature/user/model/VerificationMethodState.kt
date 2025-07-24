package `in`.koreatech.koin.feature.user.model

sealed class VerificationMethodState {
    data object None : VerificationMethodState()
    data class Sent(val remainingCount: Int, val totalCount: Int, val currentCount: Int) : VerificationMethodState()
    data object WrongFormat : VerificationMethodState()
    data object Available : VerificationMethodState()
    data object AlreadySignedUp : VerificationMethodState()
    data object CountExceeded : VerificationMethodState()
    data object NotFound : VerificationMethodState()
    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ) : VerificationMethodState()
}
