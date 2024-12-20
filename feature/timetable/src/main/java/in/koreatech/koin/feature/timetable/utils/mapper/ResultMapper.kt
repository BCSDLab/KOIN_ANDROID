package `in`.koreatech.koin.feature.timetable.utils.mapper

import `in`.koreatech.koin.domain.util.DataError
import `in`.koreatech.koin.domain.util.Result
import `in`.koreatech.koin.feature.timetable.utils.UiText

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}