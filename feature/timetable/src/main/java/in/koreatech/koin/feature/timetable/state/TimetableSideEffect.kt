package `in`.koreatech.koin.feature.timetable.state

import androidx.annotation.StringRes

sealed class TimetableSideEffect {
    data class SnackBar(
        @StringRes val messageRes: Int
    ) : TimetableSideEffect()

    data class Toast(
        val message: String
    ) : TimetableSideEffect()

    data object Nothing : TimetableSideEffect()
}
