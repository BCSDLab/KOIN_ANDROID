package `in`.koreatech.koin.ui.navigation.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class GotoAskFormContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(BCSD_ASK_FORM_URL))
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }

    companion object {
        const val BCSD_ASK_FORM_URL = "https://forms.gle/hE4VMchTZuff5rLB7"
    }
}