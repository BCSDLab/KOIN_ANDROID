package `in`.koreatech.koin.core.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.progressdialog.CustomProgressDialog
import `in`.koreatech.koin.core.progressdialog.IProgressDialog


abstract class ActivityBase : AppCompatActivity, IProgressDialog {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var customProgressDialog: CustomProgressDialog? = null
    protected abstract val screenTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
    }

    override fun showProgressDialog(message: String?) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(this, message)
            customProgressDialog!!.execute()
        }
    }

    override fun showProgressDialog(@StringRes resId: Int) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(this, getResources().getString(resId))
            customProgressDialog!!.execute()
        }
    }

    override fun hideProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog!!.cancel(false)
            customProgressDialog = null
        }
    }

    override fun onResume() {
        super.onResume()
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(PAGE_TITLE, screenTitle)
        }
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    protected fun logClickEvent(action: String, label: String, value: String) {
        logEvent(action, AnalyticsConstant.Category.CLICK, label, value)
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     */
    protected fun logScrollEvent(action: String, label: String, value: String) {
        logEvent(action, AnalyticsConstant.Category.SCROLL, label, value)
    }

    /**
     * @param action: 이벤트 발생 도메인(BUSINESS, CAMPUS, USER)
     * @param category: 이벤트 종류(click, scroll, ...)
     * @param label: 이벤트 소분류
     * @param value: 이벤트 값
     * @sample logEvent("BUSINESS", "click", "main_store_categories", "전체보기")
     */
    private fun logEvent(action: String, category: String, label: String, value: String) {
        Firebase.analytics.logEvent(action) {
            param("event_category", category)
            param("event_label", label)
            param("value", value)
        }
    }

    companion object {
        const val INPUT_METHOD_SERVICE: String = Context.INPUT_METHOD_SERVICE
        const val PAGE_TITLE = "page_title"
    }
}

