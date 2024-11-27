package `in`.koreatech.koin.feature.timetable.state

sealed class TimetableSideEffect {
    data class SnackBar(
        val message: String
    ) : TimetableSideEffect()

    data object Nothing : TimetableSideEffect()
}
