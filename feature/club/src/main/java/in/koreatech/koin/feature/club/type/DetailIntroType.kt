package `in`.koreatech.koin.feature.club.type

import `in`.koreatech.koin.feature.club.R

enum class DetailIntroType(
    val strResId: Int
) {
    DETAIL_CATEGORY(R.string.detail_intro_category),
    DETAIL_LOCATION(R.string.detail_intro_location),
    DETAIL_DESCRIPTION(R.string.detail_intro_introduction),
    DETAIL_INSTAGRAM(R.string.detail_intro_instagram),
    DETAIL_GOOGLE_FORM(R.string.detail_intro_google_form),
    DETAIL_OPEN_CHAT(R.string.detail_intro_open_chat),
    DETAIL_PHONE_NUMBER(R.string.detail_intro_phone_number)
}
