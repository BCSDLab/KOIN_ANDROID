package `in`.koreatech.koin.ui.store.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.ui.store.activity.StoreActivity
import `in`.koreatech.koin.ui.store.activity.StoreDetailActivity

class StoreDetailActivityContract(
    private val storeCategoryFactory: StoreActivity.StoreCategoryFactory? = null
) : ActivityResultContract<Int, Unit>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, StoreDetailActivity::class.java).apply {
            putExtra(STORE_ID, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {
        println("EventLogger: ${resultCode==Activity.RESULT_OK}, $intent")
        if (resultCode == Activity.RESULT_OK) {
            val category = storeCategoryFactory?.getCurrentCategory() ?: return
            val elapsedTime = intent?.getLongExtra(StoreDetailActivity.ELAPSED_TIME, -1)
            val storeName = intent?.getStringExtra(StoreDetailActivity.STORE_NAME)
            val backAction = intent?.getStringExtra(StoreDetailActivity.BACK_ACTION)
            if (backAction == "swipe")
                EventLogger.logSwipeEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
                    storeName ?: "Unknown",
                    EventExtra(AnalyticsConstant.CURRENT_PAGE, category),
                    EventExtra(AnalyticsConstant.DURATION_TIME, elapsedTime.toString())
                )
            else if (backAction == "click")
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_DETAIL_VIEW_BACK,
                    storeName ?: "Unknown",
                    EventExtra(AnalyticsConstant.CURRENT_PAGE, category),
                    EventExtra(AnalyticsConstant.DURATION_TIME, elapsedTime.toString())
                )
        }
    }

    companion object {
        const val STORE_ID = "STORE_ID"
    }
}