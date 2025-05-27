package `in`.koreatech.koin.ui.screens.store.components

import `in`.koreatech.koin.R

fun iconNameToDrawableRes(iconName: String): Int {
    return when (iconName) {
        "home" -> R.drawable.ic_home
        "nearby" -> R.drawable.ic_pin
        "orderHistory" -> R.drawable.ic_hamburger_menu
        else -> R.drawable.ic_article_reported
    }
}
