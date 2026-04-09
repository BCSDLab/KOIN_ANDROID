package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.utils.EXTRA_POST_ID

@AndroidEntryPoint
class GroupChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
            // 화면 상태 저장 이후에는 방향 고정이 불가능할 수 있어 무시한다.
        }
        setContent {
            RebrandKoinTheme {
                GroupChatScreen()
            }
        }
    }

    companion object {
        fun createIntent(context: Context, postId: Int): Intent = Intent(context, GroupChatActivity::class.java).apply {
            putExtra(EXTRA_POST_ID, postId)
        }
    }
}
