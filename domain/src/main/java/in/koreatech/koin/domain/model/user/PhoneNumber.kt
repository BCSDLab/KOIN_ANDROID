package `in`.koreatech.koin.domain.model.user

sealed class PhoneNumber {
    data object None : PhoneNumber()
    data class Sent(val remainingCount: Int, val totalCount: Int, val currentCount: Int) : PhoneNumber()
    data object WrongFormat : PhoneNumber()
    data object Available : PhoneNumber()
    data object AlreadySignedUp : PhoneNumber()
    data object CountExceeded : PhoneNumber()
    data object NotFound : PhoneNumber()
    data class Failed(
        val message: String = "",
        val throwable: Throwable? = null
    ) : PhoneNumber()
}
