package `in`.koreatech.koin.feature.chat.ui.list

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity

@AndroidEntryPoint
class ChatListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            try {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (ignore: IllegalStateException) {
            }
            KoinTheme {
                ChatList(
                    showBlockedMessage = intent.extras?.getBoolean(ChatRoomActivity.IS_BLOCKED)
                        ?: false
                )
            }
        }
    }
}
