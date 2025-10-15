package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class FilterBadge(@StringRes val stringResId: Int) {
    PICKUP_AVAILABLE(R.string.filter_badge_pickup_available),
    DELIVERY_AVAILABLE(R.string.filter_badge_delivery_available),
    SERVICE(R.string.filter_badge_service)
}
