package `in`.koreatech.koin.feature.timetable.utils.mapper

import `in`.koreatech.koin.domain.util.DataError
import `in`.koreatech.koin.feature.timetable.R
import `in`.koreatech.koin.feature.timetable.utils.UiText

fun DataError.asUiText(): UiText {
    return when (this) {
        is DataError.Network.ServerErrorWithMessage -> UiText.DynamicString(this.message)
        DataError.Network.TooManyRequest -> UiText.StringResource(R.string.network_error_too_many_request)
        DataError.Network.PayloadTooLarge -> UiText.StringResource(R.string.network_error_payload_too_large)
        DataError.Network.RequestTimeout -> UiText.StringResource(R.string.network_error_request_timeout)
        DataError.Network.NoInternet -> UiText.StringResource(R.string.network_error_no_internet)
        DataError.Network.ServerError -> UiText.StringResource(R.string.network_error_server_error)
        DataError.Network.Unknown -> UiText.StringResource(R.string.network_error_unknown)

        DataError.Local.DiskFull -> UiText.StringResource(R.string.local_error_disk_full)
        DataError.Local.Unknown -> UiText.StringResource(R.string.local_error_unknown)
    }
}