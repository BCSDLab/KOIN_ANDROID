package `in`.koreatech.koin.ui.store.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.ui.store.activity.StoreActivity

class StoreActivityContract : ActivityResultContract<StoreCategory?, Unit>() {
    override fun createIntent(context: Context, input: StoreCategory?): Intent {
        return Intent(context, StoreActivity::class.java).apply {
            putExtra(STORE_CATEGORY, input?.toString())
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }

    companion object {
        const val STORE_CATEGORY = "STORE_CATEGORY"
    }
}