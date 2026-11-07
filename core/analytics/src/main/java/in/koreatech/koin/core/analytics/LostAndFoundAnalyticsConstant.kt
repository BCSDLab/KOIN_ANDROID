package `in`.koreatech.koin.core.analytics

object LostAndFoundAnalyticsConstant {
    const val EVENT_NAME = "DA2"

    object Label {
        const val CATEGORY_ENTRY: String = "board__main_category__lost_item_entry__category_entry__click"
        const val LIST_WRITE_ENTRY = "board__lost_item_list__lost_item_write__entry__click"
        const val LIST_WRITE_POST_TYPE = "board__lost_item_list__lost_item_write__post_type__select"
        const val LIST_FILTER_OPEN = "board__lost_item_list__filter__open__click"
        const val LIST_FILTER_APPLY = "board__lost_item_list__filter__apply__click"
        const val LIST_POST_ENTRY = "board__lost_item_list__lost_item_detail__entry__click"
        const val LIST_KEYWORD_MANAGE_ENTRY = "board__lost_item_list__keyword_manage__entry__click"

        const val DETAIL_DELETE_OPEN = "board__lost_item_detail__lost_item_delete__open__click"
        const val DETAIL_DELETE_CONFIRM = "board__lost_item_detail__lost_item_delete__confirm__click"
        const val DETAIL_MESSAGE_ENTRY = "board__lost_item_detail__lost_item_message__entry__click"
        const val DETAIL_REPORT_ENTRY = "board__lost_item_detail__lost_item_report__entry__click"
        const val DETAIL_STATUS_OPEN = "board__lost_item_detail__lost_item_status__open__click"
        const val DETAIL_STATUS_FOUND_CONFIRM = "board__lost_item_detail__lost_item_status__found_confirm__click"
        const val DETAIL_EDIT_ENTRY = "board__lost_item_detail__lost_item_edit__entry__click"

        const val LOST_ITEM_WRITE_SUBMIT = "board__lost_item_write__lost_item_write__submit"
        const val LOST_ITEM_WRITE_ITEM_ADD = "board__lost_item_write__lost_item_write__item_add__click"
        const val LOST_ITEM_WRITE_ITEM_CATEGORY = "board__lost_item_write__lost_item_write__item_category__select"

        const val FOUND_ITEM_WRITE_SUBMIT = "board__found_item_write__lost_item_write__submit"
        const val FOUND_ITEM_WRITE_ITEM_ADD = "board__found_item_write__lost_item_write__item_add__click"
        const val FOUND_ITEM_WRITE_ITEM_CATEGORY = "board__found_item_write__lost_item_write__item_category__select"

        const val LOST_ITEM_EDIT_SUBMIT = "board__lost_item_edit__lost_item_edit__submit"
        const val FOUND_ITEM_EDIT_SUBMIT = "board__found_item_edit__lost_item_edit__submit"

        const val REPORT_SUBMIT = "board__lost_item_report__lost_item_report__submit"

        const val KEYWORD_MANAGE_ADD = "board__lost_item_keyword_manage__keyword_manage__add__submit"
        const val KEYWORD_MANAGE_RECOMMENDED_KEYWORD = "board__lost_item_keyword_manage__keyword_manage__recommended_keyword__select"
        const val KEYWORD_MANAGE_NOTIFICATION = "board__lost_item_keyword_manage__keyword_manage__notification__click"
        const val KEYWORD_MANAGE_DELETE = "board__lost_item_keyword_manage__keyword_manage__delete__click"

        const val DETAIL_LOGIN_POPUP_LOGIN = "account__lost_item_detail__login_popup__login_entry__click"
        const val DETAIL_LOGIN_POPUP_CLOSE = "account__lost_item_detail__login_popup__close__click"
        const val LIST_LOGIN_POPUP_LOGIN = "account__lost_item_list__login_popup__login_entry__click"
        const val LIST_LOGIN_POPUP_CLOSE = "account__lost_item_list__login_popup__close__click"
    }

    object Value {
        const val WRITE = "글쓰기"
        const val ITEM_ADD = "물품 추가"
        const val SUBMIT = "작성완료"
        const val CONFIRM = "확인"
        const val REPORT = "신고하기"
        const val LOGIN = "로그인하기"
        const val CLOSE = "닫기"
        const val LOST = "분실물"
        const val FOUND = "습득물"
        const val LOST_MESSAGE = "분실물 쪽지 보내기"
        const val FOUND_MESSAGE = "습득물 쪽지 보내기"
        const val POST_TYPE_LOST = "잃어버렸어요"
        const val POST_TYPE_FOUND = "주인을 찾아요"
        const val KEYWORD_SETTING = "키워드 설정"
        const val KEYWORD_ADD = "키워드 추가"
        const val NOTIFICATION_ON = "받기"
        const val NOTIFICATION_OFF = "끄기"
        const val ALL = "전체"
        const val MY_POST = "내 게시물"
        const val STATUS_FINDING = "찾는중"
        const val STATUS_FOUND = "찾음"
    }

    object Category {
        const val SELECT = "select"
        const val SUBMIT = "submit"
    }

    object ExtraKey {
        const val FILTER_SCOPE = "filter_scope"
        const val FILTER_POST_TYPE = "filter_post_type"
        const val FILTER_ITEM_TYPE = "filter_item_type"
        const val FILTER_STATUS = "filter_status"
    }
}
