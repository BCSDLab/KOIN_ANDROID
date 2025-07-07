package `in`.koreatech.koin.feature.club.type

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.club.R

enum class DetailTabType(
    @StringRes val strResId: Int
) {
    DETAIL_INTRO(R.string.detail_tab_detail),
    RECRUIT(R.string.detail_tab_recruit),
    EVENT(R.string.detail_tab_event),
    QNA(R.string.detail_tab_qna)
}
