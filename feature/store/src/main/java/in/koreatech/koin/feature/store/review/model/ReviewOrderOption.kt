package `in`.koreatech.koin.feature.store.review.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class ReviewOrderOption(@StringRes val stringResId: Int) {
    RECENT(R.string.review_order_option_recent),
    LEAST_RECENT(R.string.review_order_option_least_recent),
    HIGHER_RATING(R.string.review_order_option_higher_rating),
    LOWER_RATING(R.string.review_order_option_lower_rating)
}
