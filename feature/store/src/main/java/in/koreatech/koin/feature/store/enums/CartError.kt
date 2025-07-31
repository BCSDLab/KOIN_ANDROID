package `in`.koreatech.koin.feature.store.enums

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.store.R

enum class CartError(@StringRes val message: Int) {
    NONE(0),
    DIFFERENT_SHOP_ITEM_IN_CART(R.string.store_cart_add_different_shop_item_in_cart),
    MENU_SOLD_OUT(R.string.store_cart_add_menu_sold_out),
    REQUIRED_OPTION_GROUP_MISSING(R.string.store_cart_add_required_option_group_missing),
    MIN_SELECTION_NOT_MET(R.string.store_cart_add_min_selection_not_met),
    MAX_SELECTION_EXCEEDED(R.string.store_cart_add_max_selection_exceeded),
    INVALID_MENU_IN_SHOP(R.string.store_cart_add_invalid_menu_in_shop),
    SHOP_CLOSED(R.string.store_cart_add_shop_closed)
}
