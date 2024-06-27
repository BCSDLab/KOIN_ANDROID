package `in`.koreatech.koin.ui.timetablev2

sealed class TimetableSideEffect {
    data class Toast(val message: String): TimetableSideEffect()
}