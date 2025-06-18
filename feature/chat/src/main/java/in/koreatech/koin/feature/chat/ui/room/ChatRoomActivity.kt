package `in`.koreatech.koin.feature.chat.ui.room

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.chat.ui.list.ChatListActivity

@AndroidEntryPoint
class ChatRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            try {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (ignore: IllegalStateException) {
            }
            KoinTheme {
                ChatRoom(
                    navigateToChatList = {
                        Intent(this, ChatListActivity::class.java).apply {
                            putExtra(IS_BLOCKED, it)
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }.let(::startActivity)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val IS_BLOCKED = "is_blocked"
    }
}
