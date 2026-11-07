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
        const val SHOP_CATEGORIES_SEARCH_CLICK = "shop_categories_search_click"
        const val SHOP_CATEGORIES_BENEFIT = "shop_categories_benefit"
        const val SHOP_CATEGORIES_BACK = "shop_categories_back"
        const val HAMBURGER = "hamburger"
        const val HAMBURGER_SHOP = HAMBURGER
        const val HAMBURGER_DINING = "$HAMBURGER"
        const val HAMBURGER_BUS = "$HAMBURGER"
        const val MAIN_MENU_MOVEDETAILVIEW = "main_menu_moveDetailView"
        const val MAIN_MENU_CORNER = "main_menu_corner"
        const val BUS_TAB_MENU = "bus_tab_menu"
        const val BUS_SEARCH_DEPARTURE = "bus_search_departure"
        const val BUS_SEARCH_ARRIVAL = "bus_search_arrival"
        const val BUS_SEARCH = "bus_search"
        const val MAIN_BUS = "main_bus"
        const val MAIN_BUS_CHANGETOFROM = "main_bus_changeToFrom"
        const val MAIN_BUS_SCROLL = "main_bus_scroll"
        const val BUS_DEPARTURE = "bus_departure"
        const val BUS_ARRIVAL = "bus_arrival"
        const val SHOP_PICTURE = "shop_picture"

        const val SHOP_PICTURE_SWIPE = "shop_picture_swipe"
        const val SHOP_DETAIL_VIEW_INFO = "shop_detail_view_info"
        const val SHOP_DETAIL_VIEW_EVENT = "shop_detail_view_event"
        const val SHOP_BENEFIT_BACK = "shop_benefit_back"
        const val SHOP_BENEFIT_DETAIL = "shop_benefit_detail"

        const val SHOP_DETAIL_VIEW_REVIEW_REPORT_DONE = "shop_detail_view_review_report_done"

        const val SHOP_DETAIL_VIEW_REVIEW_CAN = "shop_detail_view_review_can"

        const val SHOP_DETAIL_VIEW_REVIEW_BACK = "shop_detail_view_review_back"
        const val NOTIFICATION = "notification"

        const val BENEFIT_SHOP_CATEGORIES = "benefit_shop_categories"
        const val BENEFIT_SHOP_CATEGORIES_EVENT = "benefit_shop_categories_event"
        const val BENEFIT_SHOP_CLICK = "benefit_shop_click"
        const val BENEFIT_SHOP_CALL = "benefit_shop_call"

        const val APP_MAIN_NOTICE_DETAIL = "app_main_notice_detail"
        const val POPULAR_NOTICE_BANNER = "popular_notice_banner"
        const val TO_MANAGE_KEYWORD = "to_manage_keyword"

        const val LOGIN_PROMPT = "login_prompt"

        object LostAndFound {
            const val LOST_ITEM_CATEGORY = "lost_item_category"
            const val FIND_USER_CATEGORY = "find_user_category"
        }

        object CHAT {
            const val HAMBURGER = "hamburger"
        }

        object Club {
            const val MAIN_POPULAR_CLUB = "main_popular_club"
            const val MAIN_CLUB = "main_club"
            const val MAIN_SELECT_CLUB = "club_main_select"
            const val MAIN_CLUB_CREATE = "club_main_create"
            const val MAIN_CLUB_CATEGORY = "club_main_category"
            const val MAIN_CLUB_LIKE = "club_main_like"
            const val MAIN_CLUB_LIKE_CANCEL = "club_main_like_cancel"
            const val CLUB_CREATE_CANCEL = "club_create_cancel"
            const val CLUB_CREATE_REQUEST = "club_create_request"
            const val CLUB_CREATE_REQUEST_CHECK = "club_create_request_check"
            const val CLUB_CREATE_REQUEST_AUTHORITY = "club_create_request_authority"
            const val CLUB_QNA_ADD = "club_Q&A_add"
            const val CLUB_QNA_ADD_CONFIRM = "club_Q&A_add_confirm"
            const val CLUB_QNA_DELETE_CONFIRM = "club_Q&A_delete_confirm"
            const val CLUB_CORRECTION = "club_correction"
            const val CLUB_CORRECTION_SAVE = "club_correction_save"
            const val CLUB_CORRECTION_CANCEL = "club_correction_cancel"
            const val CLUB_DELEGATION_AUTHORITY = "club_delegation_authority"
            const val CLUB_DELEGATION_AUTHORITY_CONFIRM = "club_delegation_authority_confirm"
            const val CLUB_TAB_SELECT = "club_tab_select"
            const val CLUB_INTRODUCTION_LIKE = "club_introduction_like"
            const val CLUB_INTRODUCTION_LIKE_CANCEL = "club_introduction_like_cancel"
            const val CLUB_RECRUITMENT_NOTI = "club_recruitment_noti"
            const val CLUB_RECRUITMENT_ACCEPT = "club_recruitment_accept"
            const val CLUB_RECRUITMENT_CANCEL = "club_recruitment_cancel"
        }

        object Callvan {
            const val MAIN_CALLVAN_VIEW = "main_callvan_view"
            const val MAIN_CALLVAN_WRITE = "main_callvan_write"
            const val CALLVAN_HAMBURGER = "hamburger"
        }

        object Category {
            const val CATEGORY_TEAM_RECRUITMENT = "category_team_recruitment"
            const val CATEGORY_DINING = "category_dining"
            const val CATEGORY_BUS_TIMETABLE = "category_bus_timetable"
            const val CATEGORY_TRANSPORTATION = "category_transportation"
            const val CATEGORY_KOIN_FOR_BUSINESS = "category_koin_for_business"
        }

        object TeamRecruitment {
            const val NOTIFICATION = "team_recruitment_notification"

            const val SEARCH = "team_recruitment_search"
            const val FILTER = "team_recruitment_filter"
            const val FILTER_STATUS = "team_recruitment_filter_status"
            const val FILTER_SORT = "team_recruitment_filter_sort"
            const val FILTER_CATEGORY = "team_recruitment_filter_category"
            const val FILTER_METHOD = "team_recruitment_filter_method"
            const val FILTER_RESET = "team_recruitment_filter_reset"
            const val FILTER_APPLY = "team_recruitment_filter_apply"
            const val POST_SELECT = "team_recruitment_post_select"

            const val POST_APPLY = "team_recruitment_post_apply"
            const val POST_APPLICANT_CHECK = "team_recruitment_post_applicant_check"

            const val POST_DELETE = "team_recruitment_post_delete"
            const val POST_DELETE_CONFIRM = "team_recruitment_post_delete_confirm"
            const val POST_DELETE_CANCEL = "team_recruitment_post_delete_cancel"

            const val POST_EDIT = "team_recruitment_post_edit"
            const val POST_EDIT_CATEGORY = "team_recruitment_post_edit_category"
            const val POST_EDIT_METHOD = "team_recruitment_post_edit_method"
            const val POST_EDIT_ROLE = "team_recruitment_post_edit_role"
            const val POST_EDIT_SUBMIT = "team_recruitment_post_edit_submit"
            const val POST_EDIT_SUBMIT_CANCEL = "team_recruitment_post_edit_submit_cancel"
            const val POST_EDIT_SUBMIT_CONFIRM = "team_recruitment_post_edit_submit_confirm"

            const val RECRUIT = "team_recruitment_recruit"
            const val RECRUIT_CATEGORY = "team_recruitment_recruit_category"
            const val RECRUIT_METHOD = "team_recruitment_recruit_method"
            const val RECRUIT_ROLE = "team_recruitment_recruit_role"
            const val RECRUIT_SUBMIT = "team_recruitment_recruit_submit"
            const val RECRUIT_SUBMIT_CANCEL = "team_recruitment_recruit_submit_cancel"
            const val RECRUIT_SUBMIT_CONFIRM = "team_recruitment_recruit_submit_confirm"

            const val APPLY_LOAD = "team_recruitment_apply_load"
            const val APPLY_MAJOR_SELECT = "team_recruitment_apply_major_select"
            const val APPLY_SKILL_ADD = "team_recruitment_apply_skill_add"
            const val APPLY_ACTIVITY_ADD = "team_recruitment_apply_activity_add"
            const val APPLY_ACTIVITY_MODIFY = "team_recruitment_apply_activity_modify"
            const val APPLY_ACTIVITY_MODIFY_COMPLETE = "team_recruitment_apply_activity_modify_complete"
            const val APPLY_NEXT = "team_recruitment_apply_next"
            const val APPLY_ROLE_SELECT = "team_recruitment_apply_role_select"
            const val APPLY_SUBMIT = "team_recruitment_apply_submit"
            const val APPLY_SUBMIT_CONFIRM = "team_recruitment_apply_submit_confirm"
            const val APPLY_SUBMIT_CANCEL = "team_recruitment_apply_submit_cancel"

            const val PROFILE = "team_recruitment_profile"
            const val PROFILE_MODIFY = "team_recruitment_profile_modify"
            const val PROFILE_CREATE = "team_recruitment_profile_create"
            const val PROFILE_CREATED = "team_recruitment_profile_created"
            const val PROFILE_APPLIED = "team_recruitment_profile_applied"

            const val PROFILE_MODIFY_LOAD = "team_recruitment_profile_modify_load"
            const val PROFILE_MODIFY_MAJOR_SELECT = "team_recruitment_profile_modify_major_select"
            const val PROFILE_MODIFY_NEXT = "team_recruitment_profile_modify_next"
            const val PROFILE_MODIFY_SKILL_ADD = "team_recruitment_profile_modify_skill_add"
            const val PROFILE_MODIFY_ACTIVITY_MODIFY = "team_recruitment_profile_modify_activity_modify"
            const val PROFILE_MODIFY_ACTIVITY_MODIFY_COMPLETE = "team_recruitment_profile_modify_activity_modify_complete"
            const val PROFILE_MODIFY_ACTIVITY_ADD = "team_recruitment_profile_modify_activity_add"
            const val PROFILE_MODIFY_ACTIVITY_COMPLETE = "team_recruitment_profile_modify_activity_complete"
            const val PROFILE_MODIFY_SUBMIT = "team_recruitment_profile_modify_submit"
            const val PROFILE_MODIFY_SUBMIT_CONFIRM = "team_recruitment_profile_modify_submit_confirm"
            const val PROFILE_MODIFY_SUBMIT_CANCEL = "team_recruitment_profile_modify_submit_cancel"

            const val PROFILE_CREATE_LOAD = "team_recruitment_profile_create_load"
            const val PROFILE_CREATE_MAJOR = "team_recruitment_profile_create_major"
            const val PROFILE_CREATE_NEXT = "team_recruitment_profile_create_next"
            const val PROFILE_CREATE_SKILL_ADD = "team_recruitment_profile_create_skill_add"
            const val PROFILE_CREATE_ACTIVITY_ADD = "team_recruitment_profile_create_activity_add"
            const val PROFILE_CREATE_ACTIVITY_ADD_COMPLETE = "team_recruitment_profile_create_activity_add_complete"
            const val PROFILE_CREATE_ACTIVITY_MODIFY = "team_recruitment_profile_create_activity_modify"
            const val PROFILE_CREATE_ACTIVITY_MODIFY_COMPLETE = "team_recruitment_profile_create_activity_modify_complete"
            const val PROFILE_CREATE_SUBMIT = "team_recruitment_profile_create_submit"
            const val PROFILE_CREATE_SUBMIT_CANCEL = "team_recruitment_profile_create_submit_cancel"
            const val PROFILE_CREATE_SUBMIT_CONFIRM = "team_recruitment_profile_create_submit_confirm"

            const val APPLIED_POST_FILTER = "team_recruitment_applied_post_filter"
            const val APPLIED_POST_FILTER_STATUS = "team_recruitment_applied_post_filter_status"
            const val APPLIED_POST_FILTER_SORT = "team_recruitment_applied_post_filter_sort"
            const val APPLIED_POST_FILTER_RESET = "team_recruitment_applied_post_filter_reset"
            const val APPLIED_POST_FILTER_APPLY = "team_recruitment_applied_post_filter_apply"
            const val APPLIED_POST_CHAT = "team_recruitment_applied_post_chat"

            const val CREATED_POST_APPLICANT = "team_recruitment_created_post_applicant"
            const val CREATED_POST_CLOSE = "team_recruitment_created_post_close"
            const val CREATED_POST_CLOSE_CANCEL = "team_recruitment_created_post_close_cancel"
            const val CREATED_POST_CLOSE_CONFIRM = "team_recruitment_created_post_close_confirm"
            const val CREATED_POST_CHAT = "team_recruitment_created_post_chat"

            const val CREATED_POST_FILTER = "team_recruitment_created_post_filter"
            const val CREATED_POST_FILTER_STATUS = "team_recruitment_created_post_filter_status"
            const val CREATED_POST_FILTER_SORT = "team_recruitment_created_post_filter_sort"
            const val CREATED_POST_FILTER_RESET = "team_recruitment_created_post_filter_reset"
            const val CREATED_POST_FILTER_APPLY = "team_recruitment_created_post_filter_apply"

            const val CREATED_POST_APPLICANT_SELECT = "team_recruitment_created_post_applicant_select"
            const val CREATED_POST_APPLICANT_CHAT = "team_recruitment_created_post_applicant_chat"
            const val CREATED_POST_APPLICANT_APPROVE = "team_recruitment_created_post_applicant_approve"
            const val CREATED_POST_APPLICANT_APPROVE_CONFIRM = "team_recruitment_created_post_applicant_approve_confirm"
            const val CREATED_POST_APPLICANT_APPROVE_CANCEL = "team_recruitment_created_post_applicant_approve_cancel"
            const val CREATED_POST_APPLICANT_REJECT = "team_recruitment_created_post_applicant_reject"
            const val CREATED_POST_APPLICANT_REJECT_CONFIRM = "team_recruitment_created_post_applicant_reject_confirm"
            const val CREATED_POST_APPLICANT_REJECT_CANCEL = "team_recruitment_created_post_applicant_reject_cancel"
        }

        object Department {
            const val DEPARTMENT_SEARCH = "department_search"
            const val DEPARTMENT_CATEGORY = "department_category"
            const val DEPARTMENT_CONTACT_COPY = "department_contact_copy"
        }

        object DiningSoldOut {
            const val EVENT_NAME = "DA1"

            const val CATEGORY_EXPOSURE = "exposure"
            const val CATEGORY_CLICK = "click"
            const val CATEGORY_SELECT = "select"
            const val CATEGORY_SUBMIT = "submit"
            const val CATEGORY_FAIL = "fail"

            const val EXPOSURE = "cafeteria__dining__abtest__exposure"
            const val START = "cafeteria__dining__soldout__start"
            const val SELECT = "cafeteria__dining__soldout__select"
            const val PHOTO = "cafeteria__dining__soldout__photo"
            const val SUBMIT = "cafeteria__dining__soldout__submit"
            const val EXIT = "cafeteria__dining__soldout__exit"

            const val GROUP_A = "A안"
            const val GROUP_B = "B안"
            const val EXIT_SUFFIX = "_이탈"
        }
    }

    const val PREVIOUS_PAGE = "previous_page"
    const val CURRENT_PAGE = "current_page"
    const val DURATION_TIME = "duration_time"
}
