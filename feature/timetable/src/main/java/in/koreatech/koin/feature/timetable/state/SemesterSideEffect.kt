package `in`.koreatech.koin.feature.timetable.state

sealed class SemesterSideEffect {
    data class SnackBar(
        val message: String
    ) : SemesterSideEffect()

    data class Toast(
        val message: String
    ) : SemesterSideEffect()

    data object Nothing : SemesterSideEffect()
}
