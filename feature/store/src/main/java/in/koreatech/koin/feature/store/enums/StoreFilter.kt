package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class StoreFilter(@StringRes val stringResId: Int, @DrawableRes val iconResId: Int) {
    IS_OPEN(R.string.store_filter_is_open, iconResId = R.drawable.ic_store_filter_open),
    DELIVERY_AVAILABLE(R.string.store_filter_delivery_available, iconResId = R.drawable.ic_store_filter_delivery_available),
    TAKEOUT_AVAILABLE(R.string.store_filter_takeout_available, iconResId = R.drawable.ic_store_filter_takeout_available),
    FREE_DELIVERY_TIP(R.string.store_filter_free_delivery_tip, iconResId = R.drawable.ic_store_filter_free_delivery_tip)
}

val storeFilters = listOf(
    StoreFilter.IS_OPEN,
    StoreFilter.DELIVERY_AVAILABLE,
    StoreFilter.TAKEOUT_AVAILABLE,
    StoreFilter.FREE_DELIVERY_TIP
)
