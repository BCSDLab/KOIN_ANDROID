package `in`.koreatech.koin.ui.store.components

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.store.IconType

fun iconNameToDrawableRes(iconName: IconType): Int {
    return iconName.toDrawableRes()
}

fun IconType.toDrawableRes() : Int = when (this) {
    IconType.HOME -> R.drawable.ic_home
    IconType.NEARBY -> R.drawable.ic_pin
    IconType.ORDER_HISTORY -> R.drawable.ic_hamburger_menu
    IconType.DEFAULT -> R.drawable.ic_article_reported
}