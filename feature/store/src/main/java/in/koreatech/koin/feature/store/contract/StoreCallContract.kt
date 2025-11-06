package `in`.koreatech.koin.feature.store.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri

class StoreCallContract : ActivityResultContract<String, Unit>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_CALL, "tel:$input".toUri())
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {
    }
}
