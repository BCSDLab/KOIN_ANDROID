package `in`.koreatech.koin.core.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import `in`.koreatech.koin.core.BuildConfig
import `in`.koreatech.koin.core.constant.AnalyticsConstant

object EventLogger {

    private const val EVENT_CATEGORY = "event_category"
    private const val EVENT_LABEL = "event_label"
    private const val VALUE = "value"

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    fun logClickEvent(action: String, label: String, value: String) {
        logEvent(action, AnalyticsConstant.Category.CLICK, label, value)
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    fun logScrollEvent(action: String, label: String, value: String) {
        logEvent(action, AnalyticsConstant.Category.SCROLL, label, value)
    }


    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    fun logSwipeEvent(action: String, label: String, value: String) {
        logEvent(action, AnalyticsConstant.Category.SWIPE, label, value)
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param category: 이벤트 종류(click, scroll, ...)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @sample logEvent("BUSINESS", "click", "main_shop_categories", "전체보기")
     */
    private fun logEvent(action: String, category: String, label: String, value: String) {
        if (BuildConfig.IS_DEBUG) return
        Firebase.analytics.logEvent(action) {
            param(EVENT_CATEGORY, category)
            param(EVENT_LABEL, label)
            param(VALUE, value)
        }
    }
}