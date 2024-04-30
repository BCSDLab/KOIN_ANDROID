package `in`.koreatech.koin.core.analytics

import android.os.Build
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import `in`.koreatech.koin.core.BuildConfig
import `in`.koreatech.koin.core.constant.AnalyticsConstant

object EventLogger {

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
     * @param category: 이벤트 종류(click, scroll, ...)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @sample logEvent("BUSINESS", "click", "main_shop_categories", "전체보기")
     */
    private fun logEvent(action: String, category: String, label: String, value: String) {
        if(BuildConfig.IS_DEBUG) return
        Firebase.analytics.logEvent(action) {
            param("event_category", category)
            param("event_label", label)
            param("value", value)
        }
    }
}