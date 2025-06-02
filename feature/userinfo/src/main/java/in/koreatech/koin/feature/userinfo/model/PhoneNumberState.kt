package `in`.koreatech.koin.feature.userinfo.model

sealed class PhoneNumberState {
    data object None : PhoneNumberState()
    data class Sent(val leftCount: Int, val maxCount: Int) : PhoneNumberState()
    data object WrongFormat : PhoneNumberState()
    data object AlreadySignedUp : PhoneNumberState()
    data object CountExceeded : PhoneNumberState()
}