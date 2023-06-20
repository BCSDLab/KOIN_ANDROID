package `in`.koreatech.koin.ui.store.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class StoreCallContract : ActivityResultContract<String, Unit>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_CALL, Uri.parse("tel:$input"))
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }
}