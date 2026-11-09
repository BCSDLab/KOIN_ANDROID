package `in`.koreatech.koin.core.analytics

object BoardAnalyticsConstant {
    const val EVENT_NAME = "DA2"

    object Label {
        const val MAIN_NOTICE_TAB = "board__main_notice__filter__tab__select"
        const val MAIN_NOTICE_SCROLL = "board__main_notice__notice_list__scroll"
        const val MAIN_NOTICE_SEARCH_ENTRY = "board__main_notice__search__entry__click"
        const val MAIN_NOTICE_KEYWORD_MANAGE_ENTRY = "board__main_notice__keyword_manage__entry__click"
        const val MAIN_NOTICE_KEYWORD_FILTER = "board__main_notice__filter__keyword__select"
        const val MAIN_NOTICE_DETAIL_ENTRY = "board__main_notice__notice_detail__entry__click"

        const val NOTICE_DETAIL_LIST_RETURN = "board__notice_detail__navigation__notice_list_return__click"
        const val NOTICE_DETAIL_POPULAR_POST_ENTRY = "board__notice_detail__notice_detail__popular_post_entry__click"
        const val NOTICE_DETAIL_ORIGINAL_ENTRY = "board__notice_detail__external_link__original_entry__click"

        const val NOTICE_SEARCH_SUBMIT = "board__notice_search__search__submit"
        const val NOTICE_SEARCH_POPULAR_KEYWORD = "board__notice_search__search__popular_keyword__select"
        const val NOTICE_SEARCH_DETAIL_ENTRY = "board__notice_search__notice_detail__entry__click"

        const val NOTICE_KEYWORD_MANAGE_ADD = "board__notice_keyword_manage__keyword_manage__add__submit"
        const val NOTICE_KEYWORD_MANAGE_RECOMMENDED_KEYWORD = "board__notice_keyword_manage__keyword_manage__recommended_keyword__select"
        const val NOTICE_KEYWORD_MANAGE_NOTIFICATION = "board__notice_keyword_manage__keyword_manage__notification__toggle"

        const val NOTICE_KEYWORD_MANAGE_LOGIN_POPUP_LOGIN = "account__notice_keyword_manage__login_popup__login_entry__click"
        const val NOTICE_KEYWORD_MANAGE_LOGIN_POPUP_CLOSE = "account__notice_keyword_manage__login_popup__close__click"

        const val FROM_EXTERNAL_KEYWORD_NOTIFICATION_ENTRY = "board__from_external__notice_detail__keyword_notification_entry__click"

        const val NOTIFICATION_CONFIG_KEYWORD_MANAGE_ENTRY = "board__notification_config__keyword_manage__entry__click"
    }

    object Value {
        const val SEARCH = "공지글 검색"
        const val SETTINGS_ICON = "설정 아이콘"
        const val ADD_NEW_KEYWORD = "새 키워드 추가"
        const val SEE_ALL = "모두보기"
        const val LIST_RETURN = "목록으로 돌아가기"
        const val SUBMIT_SEARCH = "검색하기"
        const val NOTIFICATION_ON = "알림 켜기"
        const val NOTIFICATION_OFF = "알림 끄기"
        const val LOGIN = "로그인하기"
        const val CLOSE = "닫기"
        const val KEYWORD_MANAGE_TITLE = "공지사항 키워드 알림"
        const val SCROLL_UP = "위로"
        const val SCROLL_DOWN = "아래로"
    }

    object Category {
        const val SELECT = "select"
        const val SUBMIT = "submit"
        const val TOGGLE = "toggle"
    }
}
