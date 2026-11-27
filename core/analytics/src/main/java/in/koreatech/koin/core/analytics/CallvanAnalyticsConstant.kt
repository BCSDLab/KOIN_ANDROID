package `in`.koreatech.koin.core.analytics

object CallvanAnalyticsConstant {
    const val EVENT_NAME = "DA2"

    object Label {
        const val LIST_SEARCH_INPUT = "callvan__callvan_list__search__search_input__click"
        const val LIST_FILTER_OPEN = "callvan__callvan_list__filter__open__click"
        const val LIST_FILTER_APPLY = "callvan__callvan_list__filter__apply__click"
        const val LIST_PARTICIPATION_JOIN_RESPONSE = "callvan__callvan_list__callvan_participation__join_response__click"
        const val LIST_PARTICIPATION_CANCEL_RESPONSE = "callvan__callvan_list__callvan_participation__cancel_response__click"
        const val LIST_CALLVAN_CHAT_CARD_ENTRY = "callvan__callvan_list__callvan_chat__card_entry__click"
        const val LIST_CONTACT_PHONE = "callvan__callvan_list__contact__phone__click"
        const val LIST_CALLVAN_WRITE_ENTRY = "callvan__callvan_list__callvan_write__entry__click"
        const val LIST_NAVIGATION_BACK = "callvan__callvan_list__navigation__back__click"
        const val LIST_NOTIFICATION_ENTRY = "callvan__callvan_list__notification_list__entry__click"

        const val PARTICIPANT_LIST_CHAT_ENTRY = "callvan__callvan_participant_list__callvan_chat__participant_entry__click"

        const val WRITE_DEPARTURE_CONFIRM = "callvan__callvan_write__callvan_write__departure_confirm__click"
        const val WRITE_ARRIVAL_CONFIRM = "callvan__callvan_write__callvan_write__arrival_confirm__click"
        const val WRITE_DEPARTURE_TIME_CONFIRM = "callvan__callvan_write__callvan_write__departure_time_confirm__click"
        const val WRITE_CONFIRM = "callvan__callvan_write__callvan_write__confirm__click"
        const val WRITE_NAVIGATION_BACK = "callvan__callvan_write__navigation__back__click"
        const val CHAT_MESSAGE_SEND = "callvan__callvan_chat__callvan_chat__message_send__click"

        const val NOTIFICATION_MARK_ALL_AS_READ = "callvan__callvan_notification__notification_list__read_all__click"
        const val NOTIFICATION_DELETE_ALL_OPEN = "callvan__callvan_notification__notification_list__delete_all_open__click"
        const val NOTIFICATION_DELETE_ALL_CONFIRM = "callvan__callvan_notification__notification_list__delete_all_confirm__click"
        const val NOTIFICATION_DELETE_ALL_CANCEL = "callvan__callvan_notification__notification_list__delete_all_cancel__click"
    }

    object Value {
        const val YES = "예"
        const val NO = "아니요"
        const val SEARCH_FIELD = "검색창"
        const val FILTER = "필터"
        const val FILTER_APPLY = "필터 적용"
        const val PHONE = "전화"
        const val WRITE = "모집하기"
        const val CHAT_ROOM_ENTRY = "채팅방 입장"
        const val BACK = "뒤로가기"
        const val CHAT = "채팅"
        const val WRITE_COMPLETE = "작성 완료"
        const val MESSAGE_SEND = "메시지 전송"
        const val NOTIFICATION_ICON = "알림 아이콘"
        const val MARK_ALL_AS_READ = "모두 읽음으로 표시"
        const val DELETE_ALL = "알림 전체 삭제"
    }
}
