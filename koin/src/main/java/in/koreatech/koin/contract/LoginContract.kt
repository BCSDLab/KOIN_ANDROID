package `in`.koreatech.koin.contract

import `in`.koreatech.koin.ui.login.LoginActivity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class LoginContract : ActivityResultContract<Unit, Unit>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }
}