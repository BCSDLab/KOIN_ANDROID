package `in`.koreatech.koin.core.error.common

import `in`.koreatech.koin.domain.util.DataError
import `in`.koreatech.koin.domain.util.Result

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}