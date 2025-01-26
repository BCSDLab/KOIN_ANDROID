package `in`.koreatech.koin.feature.chat.ui.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID

@AndroidEntryPoint
class ChatRoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinTheme {
                ChatRoom(
                    articleId = intent.getIntExtra(ARTICLE_ID, 0)
                )
            }
        }
    }
}
