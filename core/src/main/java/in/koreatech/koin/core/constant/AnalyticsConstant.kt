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
        const val MAIN_SHOP_CATEGORIES = "main_shop_categories"
        const val SHOP_CATEGORIES = "shop_categories"
        const val SHOP_CATEGORIES_SEARCH = "shop_categories_search"
        const val SHOP_CATEGORIES_EVENT = "shop_categories_event"
        const val HAMBURGER = "hamburger"
        const val HAMBURGER_SHOP = HAMBURGER
        const val HAMBURGER_DINING = "${HAMBURGER}"
        const val HAMBURGER_LAND = "${HAMBURGER}"
        const val HAMBURGER_MY_INFO_WITHOUT_LOGIN = "${HAMBURGER}_my_info_without_login"
        const val HAMBURGER_MY_INFO_WITH_LOGIN = "${HAMBURGER}_my_info_with_login"
        const val HAMBURGER_BUS = "${HAMBURGER}"
        const val MAIN_MENU_MOVEDETAILVIEW = "main_menu_moveDetailView"
        const val MAIN_MENU_CORNER = "main_menu_corner"
        const val MENU_TIME = "menu_time"
        const val MAIN_BUS = "main_bus"
        const val MAIN_BUS_CHANGETOFROM = "main_bus_changeToFrom"
        const val MAIN_BUS_SCROLL = "main_bus_scroll"
        const val BUS_DEPARTURE = "bus_departure"
        const val BUS_ARRIVAL = "bus_arrival"
        const val BUS_TIMETABLE = "bus_timetable"
        const val BUS_TIMETABLE_AREA = "bus_timetable_area"
        const val BUS_TIMETABLE_TIME = "bus_timetable_time"
        const val BUS_TIMETABLE_EXPRESS = "bus_timetable_express"
        const val MENU_IMAGE = "menu_image"
        const val LOGIN = "login"
        const val START_SIGN_UP = "start_sign_up"
        const val COMPLETE_SIGN_UP = "complete_sign_up"
        const val SHOP_PICTURE = "shop_picture"
        const val SHOP_CALL = "shop_call"
        const val SHOP_CLICK = "shop_click"
        const val SHOP_DETAIL_VIEW_EVENT = "shop_detail_view_event"
        const val SHOP_DETAIL_VIEW_BACK = "shop_detail_view_back"
        const val NOTIFICATION = "notification"
        const val NOTIFICATION_SOLD_OUT = "notification_sold_out"
        const val NOTIFICATION_BREAKFAST_SOLD_OUT = "notification_breakfast_sold_out"
        const val NOTIFICATION_LUNCH_SOLD_OUT = "notification_lunch_sold_out"
        const val NOTIFICATION_DINNER_SOLD_OUT = "notification_dinner_sold_out"
    }

    const val PREVIOUS_PAGE = "previous_page"
    const val CURRENT_PAGE = "current_page"
    const val DURATION_TIME = "duration_time"
}