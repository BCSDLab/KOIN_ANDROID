package `in`.koreatech.koin.core.constant

object AnalyticsConstant {

    object Domain {
        const val BUSINESS = "BUSINESS"
        const val CAMPUS = "CAMPUS"
        const val USER = "USER"
    }

    object Category {
        const val CLICK = "click"
        const val SCROLL = "scroll"
    }

    object Label {
        const val MAIN_STORE_CATEGORIES = "main_store_categories"
        const val STORE_CATEGORIES = "store_categories"
        const val HAMBURGER = "hamburger"
        const val HAMBURGER_STORE = "${HAMBURGER}_store"
        const val HAMBURGER_DINING = "${HAMBURGER}_dining"
        const val HAMBURGER_MY_INFO_WITHOUT_LOGIN = "${HAMBURGER}_my_info_without_login"
        const val HAMBURGER_MY_INFO_WITH_LOGIN = "${HAMBURGER}_my_info_with_login"
        const val HAMBURGER_BUS = "${HAMBURGER}_bus"
        const val USER_ONLY_OK = "user_only_ok"
        const val MAIN_MENU_MOVEDETAILVIEW = "main_menu_moveDetailView"
        const val MAIN_MENU_CORNER = "main_menu_corner"
        const val MENU_TIME = "menu_time"
        const val MAIN_BUS = "main_bus"
        const val MAIN_BUS_CHANGETOFROM = "main_bus_changeToFrom"
        const val MAIN_BUS_SCROLL = "main_bus_scroll"
        const val BUS_DEPARTURE = "bus_departure"
        const val BUS_ARRIVAL = "bus_arrival"
    }

}