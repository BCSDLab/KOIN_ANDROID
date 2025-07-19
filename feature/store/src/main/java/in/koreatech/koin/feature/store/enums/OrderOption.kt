package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class OrderOption(@StringRes val stringResId: Int) {
    NONE(R.string.store_order_option_none),
    COUNT(R.string.store_order_option_review_count_desc),
    RATING(R.string.store_order_option_rating_desc)
}
