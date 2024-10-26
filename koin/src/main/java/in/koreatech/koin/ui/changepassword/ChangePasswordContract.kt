package `in`.koreatech.koin.ui.changepassword

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ChangePasswordContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, ChangePasswordActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == RESULT_PASSWORD_CHANGED
    }

    companion object {
        const val RESULT_PASSWORD_CHANGED = 1001
    }
}