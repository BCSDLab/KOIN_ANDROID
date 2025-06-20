package `in`.koreatech.koin.feature.user.model

sealed class VerificationMethod {
    data object None : VerificationMethod()
    data class Sent(val remainingCount: Int, val totalCount: Int, val currentCount: Int) : VerificationMethod()
    data object WrongFormat : VerificationMethod()
    data object Available : VerificationMethod()
    data object AlreadySignedUp : VerificationMethod()
    data object CountExceeded : VerificationMethod()
    data object NotFound : VerificationMethod()
    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ) : VerificationMethod()
}
