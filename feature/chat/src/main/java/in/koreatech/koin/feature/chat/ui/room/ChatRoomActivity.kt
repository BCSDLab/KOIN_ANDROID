package `in`.koreatech.koin.feature.chat.ui.room

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.ui.list.ChatListActivity
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.CHAT_ROOM_ID

@AndroidEntryPoint
class ChatRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            try {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (ignore: IllegalStateException) {
            }
            KoinTheme {
                ChatRoom(
                    articleId = intent.getIntExtra(ARTICLE_ID, -1),
                    chatRoomId = intent.getIntExtra(CHAT_ROOM_ID, -1),
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
