package `in`.koreatech.koin.core.analytics

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import `in`.koreatech.koin.domain.model.user.LoggerUserData
import `in`.koreatech.koin.domain.usecase.user.GetLoggerUserDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object EventLogger {
    private const val USER_GENDER = "gender"
    private const val USER_MAJOR = "major"

    private const val EVENT_CATEGORY = "event_category"
    private const val EVENT_LABEL = "event_label"
    private const val VALUE = "value"
    private const val CUSTOM_SESSION_ID = "custom_session_id"

    private var loggerUserData: LoggerUserData = LoggerUserData("", "", "")

    fun init(getLoggerUserDataUseCase: GetLoggerUserDataUseCase) {
        CoroutineScope(Dispatchers.IO).launch {
            getLoggerUserDataUseCase()
                .collectLatest {
                    loggerUserData = it
                }
        }
    }

    /**
     * 클릭 이벤트 로깅
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logClickEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logEvent(action, EventCategory.CLICK, label, value, *extras)
    }

    /** 화면 진입 이벤트 로깅
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logEntryEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logEvent(action, EventCategory.ENTRY, label, value, *extras)
    }

    /**
     * CAPMUS 클릭 이벤트 로깅
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logCampusClickEvent(
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logClickEvent(EventAction.CAMPUS, label, value, *extras)
    }

    /**
     * 스크롤 이벤트 로깅
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logScrollEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logEvent(action, EventCategory.SCROLL, label, value, *extras)
    }

    /**
     * 하단 뒤로가기 이벤트 로깅
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logSwipeEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logEvent(action, EventCategory.SWIPE, label, value, *extras)
    }

    /**
     * 푸시알림 접속 이벤트 로깅
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     */
    fun logNotificationEvent(
        action: EventAction,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        logEvent(action, EventCategory.NOTIFICATION, label, value, *extras)
    }

    /**
     * AB테스트 이벤트 로깅
     * @param category 이벤트 종류
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     */
    fun logABTestEvent(
        category: String,
        label: String,
        value: String
    ) {
        logCustomEvent(EventAction.ABTEST.value, category, label, value)
    }

    /**
     * @param action 커스텀 이벤트 발생(EventAction 이외에 action)
     * @param category 커스텀 이벤트 종류(EventCategory 이외에 category)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     *
     * ```
     * logEvent("force_update", "page_view", "forced_update_page_view", "v4.0.0")
     * ```
     */
    fun logCustomEvent(
        action: String,
        category: String,
        label: String,
        value: String
    ) {
        if (BuildConfig.IS_DEBUG) {
            Firebase.analytics.setUserId(loggerUserData.userId)
            Firebase.analytics.logEvent("${action}_debug") {
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, "${category}_debug")
                param(EVENT_LABEL, "$label (debug)")
                param(VALUE, value)
            }
        } else {
            Firebase.analytics.setUserId(loggerUserData.userId)
            Firebase.analytics.logEvent(action) {
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, category)
                param(EVENT_LABEL, label)
                param(VALUE, value)
            }
        }
        Log.d("EventLogger", "logCustomEvent: action=$action, category=$category, label=$label, value=$value")
    }

    /**
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param category 이벤트 종류(click, scroll, ...)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param extras 추가 이벤트 값
     *
     * ```
     * logEvent(EventAction.CAMPUS, EventCategory.CLICK, "main_shop_categories", "전체보기")
     * ```
     */
    private fun logEvent(
        action: EventAction,
        category: EventCategory,
        label: String,
        value: String,
        vararg extras: EventExtra
    ) {
        if (BuildConfig.IS_DEBUG) {
            Firebase.analytics.setUserId(loggerUserData.userId)
            Firebase.analytics.logEvent("${action.value}_debug") {
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, "${category.value}_debug")
                param(EVENT_LABEL, "$label (debug)")
                param(VALUE, value)
                extras.forEach {
                    param("${it.key}_debug", it.value)
                }
            }
        } else {
            Firebase.analytics.logEvent(action.value) {
                Firebase.analytics.setUserId(loggerUserData.userId)
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, category.value)
                param(EVENT_LABEL, label)
                param(VALUE, value)
                extras.forEach {
                    param(it.key, it.value)
                }
            }
        }
        Log.d("EventLogger", "logEvent: action=$action, category=$category, label=$label, value=$value, extras=$extras")
    }

    /**
     * @param action 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param category 이벤트 종류(click, scroll, ...)
     * @param label 이벤트 소분류
     * @param value 이벤트 값
     * @param customSessionId 세션 값
     * @param extras 추가 이벤트 값
     *
     * ```
     * logEvent(EventAction.CAMPUS, EventCategory.CLICK, "main_shop_categories", "전체보기")
     * ```
     */
    fun logSessionEvent(
        action: EventAction,
        category: EventCategory,
        label: String,
        value: String,
        customSessionId: String,
        vararg extras: EventExtra
    ) {
        if (BuildConfig.IS_DEBUG) {
            Firebase.analytics.setUserId(loggerUserData.userId)
            Firebase.analytics.logEvent("${action.value}_debug") {
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, "${category.value}_debug")
                param(EVENT_LABEL, "$label (debug)")
                param(VALUE, value)
                param(CUSTOM_SESSION_ID, customSessionId)
                extras.forEach {
                    param("${it.key}_debug", it.value)
                }
            }
        } else {
            Firebase.analytics.logEvent(action.value) {
                Firebase.analytics.setUserId(loggerUserData.userId)
                loggerUserData.let {
                    param(USER_GENDER, it.gender)
                    param(USER_MAJOR, it.major)
                }
                param(EVENT_CATEGORY, category.value)
                param(EVENT_LABEL, label)
                param(VALUE, value)
                param(CUSTOM_SESSION_ID, customSessionId)
                extras.forEach {
                    param(it.key, it.value)
                }
            }
        }
        Log.d("EventLogger", "logEvent: action=$action, category=$category, label=$label, value=$value, custom_session_id=$customSessionId extras=$extras")
    }
}

enum class EventAction(val value: String) {
    BUSINESS("BUSINESS"),
    CAMPUS("CAMPUS"),
    USER("USER"),
    ABTEST("AB_TEST")
}

enum class EventCategory(val value: String) {
    CLICK("click"),
    SCROLL("scroll"),
    SWIPE("swipe"), // 하단 뒤로가기(아이폰의 swipe 뒤로가기와 대응)
    NOTIFICATION("notification"),
    ENTRY("entry")
}

data class EventExtra(val key: String, val value: String)
