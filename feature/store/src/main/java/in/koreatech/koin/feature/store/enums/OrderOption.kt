package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.feature.store.R

enum class OrderOption(@StringRes val stringResId: Int) {
    NONE(R.string.store_order_option_none),
    COUNT(R.string.store_order_option_review_count_desc),
    RATING(R.string.store_order_option_rating_desc)
}

fun OrderOption.toStoreSorter(): StoreSorter {
    return when (this) {
        OrderOption.NONE -> StoreSorter.NONE
        OrderOption.COUNT -> StoreSorter.COUNT
        OrderOption.RATING -> StoreSorter.RATING
    }
}
