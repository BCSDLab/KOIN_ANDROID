package `in`.koreatech.koin.domain.model.store

enum class IconType {
    HOME,
    NEARBY,
    ORDER_HISTORY,
    DEFAULT
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val iconName: IconType
)
