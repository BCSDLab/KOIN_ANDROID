package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class MinimumPriceOption(val price: Int, @StringRes val stringRes: Int) {
    UNDER_5000(5000, R.string.store_min_price_filter_5000),
    UNDER_10000(10000, R.string.store_min_price_filter_10000),
    UNDER_15000(15000, R.string.store_min_price_filter_15000),
    UNDER_20000(20000, R.string.store_min_price_filter_20000),
    ALL(Int.MAX_VALUE, R.string.store_min_price_filter_all)
}

val minimumPriceOptions = listOf(
    MinimumPriceOption.UNDER_5000,
    MinimumPriceOption.UNDER_10000,
    MinimumPriceOption.UNDER_15000,
    MinimumPriceOption.UNDER_20000,
    MinimumPriceOption.ALL
)
