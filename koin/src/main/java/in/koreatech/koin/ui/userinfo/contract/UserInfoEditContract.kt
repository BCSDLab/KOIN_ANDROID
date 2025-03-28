package `in`.koreatech.koin.ui.userinfo.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import `in`.koreatech.koin.ui.userinfo.UserInfoEditActivity

class UserInfoEditContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, UserInfoEditActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == RESULT_USER_INFO_EDITED
    }

    companion object {
        const val RESULT_USER_INFO_EDITED = 1001
    }
}
