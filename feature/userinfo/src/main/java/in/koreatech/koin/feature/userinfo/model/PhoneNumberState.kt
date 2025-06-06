package `in`.koreatech.koin.feature.userinfo.model

sealed class PhoneNumberState {
    data object None : PhoneNumberState()
    data object Modified : PhoneNumberState()
    data class Sent(val currentCount: Int, val remainingCount: Int, val totalCount: Int) : PhoneNumberState()
    data object WrongFormat : PhoneNumberState()
    data object AlreadySignedUp : PhoneNumberState()
    data object CountExceeded : PhoneNumberState()
    data class Failed(val message: String) : PhoneNumberState()
}
