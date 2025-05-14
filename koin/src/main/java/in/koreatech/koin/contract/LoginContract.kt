package `in`.koreatech.koin.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri

class LoginContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = "koin://login/login".toUri()
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {
    }
}
