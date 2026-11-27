package `in`.koreatech.koin.feature.chat.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.navigation.ChatDomain
import `in`.koreatech.koin.core.navigation.utils.EXTRA_APPLICATION_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_CONTEXT_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_DOMAIN_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.feature.chat.navigation.ChatNavHost
import `in`.koreatech.koin.feature.chat.navigation.ChatNavType

@AndroidEntryPoint
class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (_: IllegalStateException) {
        }
        val startDestination = startDestination()
        setContent {
            RebrandKoinTheme {
                ChatNavHost(startDestination)
            }
        }
    }

    private fun startDestination(): ChatNavType {
        val domainType = intent.getStringExtra(EXTRA_CHAT_DOMAIN_TYPE)
            ?.let(ChatDomain::valueOf) ?: return ChatNavType.ChatList()
        val contextId = requireIntExtra(EXTRA_CHAT_CONTEXT_ID)
        val chatRoomId = if (intent.hasExtra(EXTRA_CHAT_ROOM_ID)) requireIntExtra(EXTRA_CHAT_ROOM_ID) else null
        val applicationId = if (intent.hasExtra(EXTRA_APPLICATION_ID)) requireIntExtra(EXTRA_APPLICATION_ID) else null
        return when (domainType) {
            ChatDomain.LOST_ITEM -> ChatNavType.Chat(domainType.name, contextId, chatRoomId)
            ChatDomain.CALLVAN -> ChatNavType.GroupChat(domainType.name, contextId)
            ChatDomain.TEAM_RECRUITMENT -> if (applicationId != null) {
                ChatNavType.Chat(domainType.name, contextId, chatRoomId, applicationId)
            } else {
                ChatNavType.GroupChat(domainType.name, contextId, chatRoomId)
            }
        }
    }

    private fun requireIntExtra(key: String): Int {
        require(intent.hasExtra(key)) { "Missing chat argument: $key" }
        return intent.getIntExtra(key, 0)
    }
}
