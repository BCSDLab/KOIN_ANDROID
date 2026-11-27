package `in`.koreatech.koin.core.analytics

object ForceUpdateAnalyticsConstant {
    const val EVENT_NAME = "DA1"

    object Category {
        const val EXPOSURE = "exposure"
        const val EXIT = "exit"
    }

    object Label {
        const val EXPOSURE = "main__forced_update__app_update__exposure"
        const val STORE_ENTRY = "main__forced_update__app_update__store_entry__click"
        const val ALREADY_DONE = "main__forced_update__app_update__already_done__click"
        const val POPUP_CLOSE = "main__forced_update__update_popup__close__click"
        const val POPUP_STORE_ENTRY = "main__forced_update__update_popup__store_entry__click"
        const val EXIT = "main__forced_update__app_update__exit"
    }

    object Value {
        const val UPDATE = "업데이트하기"
        const val ALREADY_DONE = "이미업데이트"
        const val CLOSE = "닫기"
        const val STORE = "스토어 가기"
        const val EXIT = "앱 이탈"
    }
}
