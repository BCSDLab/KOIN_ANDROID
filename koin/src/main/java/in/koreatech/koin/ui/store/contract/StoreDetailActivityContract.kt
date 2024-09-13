package `in`.koreatech.koin.ui.store.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import `in`.koreatech.koin.ui.store.activity.StoreDetailActivity

class StoreDetailActivityContract : ActivityResultContract<Pair<Int, String?>, Unit>() {
    override fun createIntent(context: Context, input: Pair<Int, String?>): Intent {
        return Intent(context, StoreDetailActivity::class.java).apply {
            putExtra(STORE_ID, input.first)
            putExtra(CATEGORY, input.second)}
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }

    companion object {
        const val STORE_ID = "STORE_ID"
        const val CATEGORY = "CATEGORY"
    }
}