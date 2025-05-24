package `in`.koreatech.koin.feature.club.type

import `in`.koreatech.koin.feature.club.R

enum class DetailIntroType(
    val strResId: Int
) {
    DETAIL_CATEGORY(R.string.club_detail_intro_category),
    DETAIL_LOCATION(R.string.club_detail_intro_location),
    DETAIL_INTRODUCTION(R.string.club_detail_intro_introduction),
    DETAIL_INSTAGRAM(R.string.club_detail_intro_instagram),
    DETAIL_GOOGLE_FORM(R.string.club_detail_intro_google_form),
    DETAIL_OPEN_CHAT(R.string.club_detail_intro_open_chat),
    DETAIL_PHONE_NUMBER(R.string.club_detail_intro_phone_number)
}