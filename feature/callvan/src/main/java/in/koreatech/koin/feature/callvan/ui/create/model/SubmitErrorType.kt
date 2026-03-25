package `in`.koreatech.koin.feature.callvan.ui.create.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R

enum class SubmitErrorType(@StringRes val messageRes: Int) {
    INVALID_REQUEST_BODY(R.string.callvan_create_submit_error_invalid_request),
    INVALID_CUSTOM_LOCATION_NAME(R.string.callvan_create_submit_error_invalid_custom_location),
    NOT_FOUND_USER(R.string.callvan_create_submit_error_not_found_user),
    UNKNOWN(R.string.callvan_create_submit_error_unknown)
}
