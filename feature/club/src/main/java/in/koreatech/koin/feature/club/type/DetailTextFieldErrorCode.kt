package `in`.koreatech.koin.feature.club.type

import `in`.koreatech.koin.feature.club.R

enum class DetailTextFieldErrorCode(
    val strResId: Int
) {
    EMPTY_ERROR(R.string.detail_textfield_error_empty),
    NON_USERID_ERROR(R.string.detail_textfield_error_non_user_id)
}
