package `in`.koreatech.koin.core.analytics

object DiningAnalyticsConstant {
    object Category {
        const val CLICK = "click"
        const val SCROLL = "scroll"
        const val SELECT = "select"
        const val TOGGLE = "toggle"
    }

    object Label {
        const val CAFETERIA_INFO_ENTRY = "cafeteria__cafeteria__cafeteria_info__entry__click"

        const val SHARE_KAKAO = "cafeteria__cafeteria__share__kakao__click"

        const val KAKAO_ENTRY = "cafeteria__from_external__cafeteria_entry__kakao_entry__click"

        const val MEAL_TIME_SELECT = "cafeteria__cafeteria__meal_time__select"

        const val MEAL_TIME_SCROLL = "cafeteria__cafeteria__meal_time__scroll"

        const val PHOTO_VIEW_OPEN = "cafeteria__cafeteria__photo_view__open__click"

        const val NOTIFICATION_CONFIG_ENTRY = "cafeteria__account_config__notification_config__entry__click"

        const val NOTIFICATION_SOLD_OUT_TOGGLE = "cafeteria__notification_config__sold_out__toggle"

        const val NOTIFICATION_SOLD_OUT_MEAL_TOGGLE = "cafeteria__notification_config__sold_out_meal__toggle"

        const val NOTIFICATION_PHOTO_UPLOAD_TOGGLE = "cafeteria__notification_config__notification_config__photo_upload__toggle"
    }

    object Value {
        const val CAFETERIA_INFO = "학생식당정보"
        const val SHARE = "공유하기"
        const val KAKAO_ENTRY = "코인으로 이동"
        const val NOTIFICATION_CONFIG = "알림설정"
        const val TOGGLE_ON = "켜짐"
        const val TOGGLE_OFF = "꺼짐"
    }
}
