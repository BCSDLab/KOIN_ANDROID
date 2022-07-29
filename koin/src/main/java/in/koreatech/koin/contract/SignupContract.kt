package `in`.koreatech.koin.contract

import `in`.koreatech.koin.ui.signup.SignupActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SignupContract: ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, SignupActivity::class.java).apply {
            putExtra(PORTAL_ACCOUNT, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return when(resultCode) {
            Activity.RESULT_OK -> {
                intent?.extras?.getString(PORTAL_ACCOUNT)
            }
            else -> null
        }
    }

    companion object {
        const val PORTAL_ACCOUNT = "portal_account"
    }
}