package `in`.koreatech.koin.feature.timetable.state

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.timetable.R

enum class TimetableMessage(@StringRes val messageRes: Int) {
    TIME_DUPLICATED(R.string.timetable_snackbar_time_duplicated),
    PLACE_COMMA_NOT_ALLOWED(R.string.timetable_snackbar_place_comma_not_allowed),
    EXTRA_CONTENT_LIMIT(R.string.timetable_snackbar_extra_content_limit)
}
