package `in`.koreatech.koin.ui.store.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import `in`.koreatech.koin.ui.store.activity.StoreDetailActivity

class StoreDetailActivityContract : ActivityResultContract<Int, Unit>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, StoreDetailActivity::class.java).apply {
            putExtra(STORE_ID, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }

    companion object {
        const val STORE_ID = "STORE_ID"
    }
}