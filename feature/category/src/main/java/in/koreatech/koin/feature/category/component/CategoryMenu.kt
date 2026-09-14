package `in`.koreatech.koin.feature.category.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.category.R
import kotlinx.collections.immutable.persistentListOf

enum class CategoryMenuId {
    TIMETABLE,
    LOST_AND_FOUND,
    OPERATING_INFO,
    DEPARTMENT_INFO,
    DINING,
    STORE,
    BUS_TIMETABLE,
    TRANSPORT_SEARCH,
    CALLVAN,
    RECRUITMENT,
    CHAT,
    HOUSING,
    KOIN_BUSINESS
}

data class CategoryMenu(
    val id: CategoryMenuId,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int
)

internal val CAMPUS_MENUS = persistentListOf(
    CategoryMenu(CategoryMenuId.OPERATING_INFO, R.drawable.ic_operating_info, R.string.campus_facility),
    CategoryMenu(CategoryMenuId.DEPARTMENT_INFO, R.drawable.ic_info, R.string.department_info),
    CategoryMenu(CategoryMenuId.DINING, R.drawable.ic_dish, R.string.dining),
    CategoryMenu(CategoryMenuId.STORE, R.drawable.ic_store_category, R.string.store),
    CategoryMenu(CategoryMenuId.TIMETABLE, R.drawable.ic_calendar_category, R.string.category_timetable)
)

internal val TRANSPORT_MENUS = persistentListOf(
    CategoryMenu(CategoryMenuId.BUS_TIMETABLE, R.drawable.ic_bus_timetable, R.string.bus_timetable),
    CategoryMenu(CategoryMenuId.TRANSPORT_SEARCH, R.drawable.ic_transport_search, R.string.transport_search),
    CategoryMenu(CategoryMenuId.CALLVAN, R.drawable.ic_user_add, R.string.callvan)
)

internal val OTHER_MENUS = persistentListOf(
    CategoryMenu(CategoryMenuId.CHAT, R.drawable.ic_category_chat, R.string.chat),
    CategoryMenu(CategoryMenuId.HOUSING, R.drawable.ic_home, R.string.housing),
    CategoryMenu(CategoryMenuId.KOIN_BUSINESS, R.drawable.ic_koin_business, R.string.koin_business)
)
