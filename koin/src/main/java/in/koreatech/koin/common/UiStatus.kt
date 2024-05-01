package `in`.koreatech.koin.common

sealed class UiStatus {
    object Init: UiStatus()
    object Loading: UiStatus()
    object Success: UiStatus()
    data class Failed(
        val message: String = ""
    ): UiStatus()
}