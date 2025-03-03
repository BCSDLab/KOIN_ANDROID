package `in`.koreatech.koin.core.analytics

object AnalyticsConstant {
    object Category {
        const val CLICK = "click"
        const val SCROLL = "scroll"
        const val SWIPE = "swipe"
    }

    object Label {
        const val MAIN_SCROLL = "main_scroll"
        const val MAIN_SHOP_CATEGORIES = "main_shop_categories"
        const val MAIN_SHOP_BENEFIT = "main_shop_benefit"
        const val SHOP_CATEGORIES = "shop_categories"
        const val SHOP_CATEGORIES_SEARCH = "shop_categories_search"
        const val SHOP_CATEGORIES_EVENT = "shop_categories_event"
        const val CAFETERIA_INFO = "cafeteria_info"
        const val HAMBURGER = "hamburger"
        const val HAMBURGER_SHOP = HAMBURGER
        const val HAMBURGER_DINING = "$HAMBURGER"
        const val HAMBURGER_BUS = "$HAMBURGER"
        const val MAIN_MENU_MOVEDETAILVIEW = "main_menu_moveDetailView"
        const val MAIN_MENU_CORNER = "main_menu_corner"
        const val MENU_TIME = "menu_time"
        const val BUS_TAB_MENU = "bus_tab_menu"
        const val BUS_SEARCH_DEPARTURE = "bus_search_departure"
        const val BUS_SEARCH_ARRIVAL = "bus_search_arrival"
        const val BUS_SEARCH = "bus_search"
        const val MAIN_BUS = "main_bus"
        const val MAIN_BUS_CHANGETOFROM = "main_bus_changeToFrom"
        const val MAIN_BUS_SCROLL = "main_bus_scroll"
        const val BUS_DEPARTURE = "bus_departure"
        const val BUS_ARRIVAL = "bus_arrival"
        const val BUS_TIMETABLE = "bus_timetable"
        const val BUS_TIMETABLE_AREA = "bus_timetable_area"
        const val BUS_TIMETABLE_CITYBUS_ROUTE = "bus_timetable_citybus_route"
        const val BUS_TIMETABLE_TIME = "bus_timetable_time"
        const val BUS_TIMETABLE_EXPRESS = "bus_timetable_express"
        const val BUS_TIMETABLE_CITYBUS = "bus_timetable_citybus"
        const val MENU_IMAGE = "menu_image"
        const val LOGIN = "login"
        const val START_SIGN_UP = "start_sign_up"
        const val COMPLETE_SIGN_UP = "complete_sign_up"
        const val SHOP_CAN = "shop_can"
        const val SHOP_PICTURE = "shop_picture"
        const val SHOP_CALL = "shop_call"
        const val SHOP_CLICK = "shop_click"
        const val SHOP_DETAIL_VIEW_BACK = "shop_detail_view_back"
        const val SHOP_DETAIL_VIEW_EVENT = "shop_detail_view_event"

        const val SHOP_DETAIL_VIEW_REVIEW = "shop_detail_view_review"
        const val SHOP_DETAIL_VIEW_REVIEW_WRITE = "shop_detail_view_review_write"
        const val SHOP_DETAIL_VIEW_REVIEW_WRITE_DONE = "shop_detail_view_review_write_done"
        const val SHOP_DETAIL_VIEW_REVIEW_REPORT = "shop_detail_view_review_report"
        const val SHOP_DETAIL_VIEW_REVIEW_REPORT_DONE = "shop_detail_view_review_report_done"

        const val SHOP_DETAIL_VIEW_REVIEW_DELETE = "shop_detail_view_review_delete"
        const val SHOP_DETAIL_VIEW_REVIEW_DELETE_DONE = "shop_detail_view_review_delete_done"
        const val SHOP_DETAIL_VIEW_REVIEW_DELETE_CANCEL = "shop_detail_view_review_delete_cancel"

        const val SHOP_DETAIL_VIEW_REVIEW_WRITE_LOGIN = "shop_detail_view_review_write_login"
        const val SHOP_DETAIL_VIEW_REVIEW_WRITE_CANCEL = "shop_detail_view_review_write_cancel"

        const val SHOP_DETAIL_VIEW_REVIEW_REPORT_LOGIN = "shop_detail_view_review_report_login"
        const val SHOP_DETAIL_VIEW_REVIEW_REPORT_CANCEL = "shop_detail_view_review_report_cancel"

        const val SHOP_DETAIL_VIEW = "shop_detail_view"

        const val SHOP_DETAIL_VIEW_REVIEW_BACK = "shop_detail_view_review_back"
        const val NOTIFICATION = "notification"
        const val NOTIFICATION_SOLD_OUT = "notification_sold_out"
        const val NOTIFICATION_BREAKFAST_SOLD_OUT = "notification_breakfast_sold_out"
        const val NOTIFICATION_LUNCH_SOLD_OUT = "notification_lunch_sold_out"
        const val NOTIFICATION_DINNER_SOLD_OUT = "notification_dinner_sold_out"
        const val NOTIFICATION_MENU_IMAGE_UPLOAD = "notification_menu_image_upload"
        const val NOTICE_TAB = "notice_tab"
        const val NOTICE_PAGE = "notice_page"
        const val INVENTORY = "inventory"
        const val POPULAR_NOTICE = "popular_notice"
        const val NOTICE_SEARCH = "notice_search"
        const val POPULAR_SEARCH_WORD = "popular_search_word"
        const val MANAGE_KEYWORD = "manage_keyword"
        const val NOTICE_FILTER_ALL = "notice_filter_all"
        const val ADD_KEYWORD = "add_keyword"
        const val RECOMMENDED_KEYWORD = "recommended_keyword"
        const val KEYWORD_NOTIFICATION = "keyword_notification"
        const val LOGIN_POPUP_KEYWORD = "login_popup_keyword"
        const val NOTICE_SEARCH_EVENT = "notice_search_event"
        const val NOTICE_ORIGINAL_SHORTCUT = "notice_original_shortcut"

        const val BENEFIT_SHOP_CATEGORIES = "benefit_shop_categories"
        const val BENEFIT_SHOP_CATEGORIES_EVENT = "benefit_shop_categories_event"
        const val BENEFIT_SHOP_CLICK = "benefit_shop_click"
        const val BENEFIT_SHOP_CALL = "benefit_shop_call"

        const val MENU_SHARE = "menu_share"

        const val APP_MAIN_NOTICE_DETAIL = "app_main_notice_detail"
        const val POPULAR_NOTICE_BANNER = "popular_notice_banner"
        const val TO_MANAGE_KEYWORD = "to_manage_keyword"

        object LostAndFound {
            const val LOST_ITEM_ADD_ITEM = "lost_item_add_item"
            const val FIND_USER_ADD_ITEM = "find_user_add_item"
            const val LOST_ITEM_CATEGORY = "lost_item_category"
            const val FIND_USER_CATEGORY = "find_user_category"
            const val LOST_WRITE = "lost_item_write"
            const val FOUND_WRITE = "find_user_write"
            const val ITEM_WRITE = "item_write"
            const val LOST_ITEM_WRITE_CONFIRM = "lost_item_write_confirm"
            const val FIND_USER_WRITE_CONFIRM = "find_user_write_confirm"
            const val FIND_USER_DELETE = "find_user_delete"
            const val FIND_USER_DELETE_CONFIRM = "find_user_delete_confirm"
            const val ITEM_POST_REPORT_CONFIRM = "item_post_report_confirm"
            const val ITEM_MESSAGE_SEND = "item_message_send"
            const val ITEM_POST_REPORT = "item_post_report"
            const val ITEM_POST_TYPE = "item_post_type"
        }

        object CHAT {
            const val HAMBURGER = "hamburger"
            const val MESSAGE_LIST_SELECT = "message_list_select"
        }
    }

    const val PREVIOUS_PAGE = "previous_page"
    const val CURRENT_PAGE = "current_page"
    const val DURATION_TIME = "duration_time"
}
