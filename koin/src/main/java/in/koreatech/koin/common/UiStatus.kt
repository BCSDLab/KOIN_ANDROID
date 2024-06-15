package `in`.koreatech.koin.common

sealed class UiStatus {
    data object Init: UiStatus()
    data object Loading: UiStatus()
    data object Success: UiStatus()
    data class Failed(
        val message: String = ""
    ): UiStatus()
}