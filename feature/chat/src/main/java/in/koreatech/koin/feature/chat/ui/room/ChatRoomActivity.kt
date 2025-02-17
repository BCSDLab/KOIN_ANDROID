package `in`.koreatech.koin.feature.chat.ui.room

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.CHAT_ROOM_ID

@AndroidEntryPoint
class ChatRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            try {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } catch (ignore: IllegalStateException) {
            }
            KoinTheme {
                ChatRoom(
                    articleId = intent.getIntExtra(ARTICLE_ID, -1),
                    chatRoomId = intent.getIntExtra(CHAT_ROOM_ID, -1)
                )
            }
        }
    }
}
