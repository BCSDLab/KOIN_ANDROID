package `in`.koreatech.koin.core.analytics

import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import `in`.koreatech.koin.core.BuildConfig
import `in`.koreatech.koin.core.analytics.EventLogger.logEvent

object EventLogger {

    private const val EVENT_CATEGORY = "event_category"
    private const val EVENT_LABEL = "event_label"
    private const val VALUE = "value"

    /**
     * 클릭 이벤트 로깅
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @param extras: 추가 이벤트 값
     */
    fun logClickEvent(action: EventAction, label: String, value: String, vararg extras: EventExtra) {
        logEvent(action, EventCategory.CLICK, label, value, *extras)
    }

    /**
     * 스크롤 이벤트 로깅
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @param extras: 추가 이벤트 값
     */
    fun logScrollEvent(action: EventAction, label: String, value: String, vararg extras: EventExtra) {
        logEvent(action, EventCategory.SCROLL, label, value, *extras)
    }

    /**
     * 하단 뒤로가기 이벤트 로깅
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @param extras: 추가 이벤트 값
     */
    fun logSwipeEvent(action: EventAction, label: String, value: String, vararg extras: EventExtra) {
        logEvent(action, EventCategory.SWIPE, label, value, *extras)
    }


    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    fun logSwipeEvent(action: EventAction, label: String, value: String) {
        logEvent(action, EventCategory.SWIPE, label, value)
    }

    /**
     * @param action: 커스텀 이벤트 발생(EventAction 이외에 action)
     * @param category: 커스텀 이벤트 종류(EventCategory 이외에 category)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @sample logEvent("force_update", "page_view", "forced_update_page_view", "v4.0.0")
     */
    fun logCustomEvent(action: String, category: String, label: String, value: String) {
        if (BuildConfig.IS_DEBUG) return
        else {
            Firebase.analytics.logEvent(action) {
                param(EVENT_CATEGORY, category)
                param(EVENT_LABEL, label)
                param(VALUE, value)
            }
        }
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param category: 이벤트 종류(click, scroll, ...)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @sample logEvent("BUSINESS", "click", "main_shop_categories", "전체보기")
     */
    private fun logEvent(action: EventAction, category: EventCategory, label: String, value: String, vararg extras: EventExtra) {
        if (BuildConfig.IS_DEBUG)
            Log.d("EventLogger", "[action: ${action.value}, category: ${category.value}, label: $label, value: $value " + extras.joinToString { it.toString() } + "]")
        else {
            Firebase.analytics.logEvent(action.value) {
                param(EVENT_CATEGORY, category.value)
                param(EVENT_LABEL, label)
                param(VALUE, value)
                extras.forEach {
                    param(it.key, it.value)
                }
            }
        }
    }
}

enum class EventAction(val value: String) {
    BUSINESS("BUSINESS"),
    CAMPUS("CAMPUS"),
    USER("USER")
}

enum class EventCategory(val value: String) {
    CLICK("click"),
    SCROLL("scroll"),
    SWIPE("swipe")      // 하단 뒤로가기(아이폰의 swipe 뒤로가기와 대응)
}

data class EventExtra(val key: String, val value: String)