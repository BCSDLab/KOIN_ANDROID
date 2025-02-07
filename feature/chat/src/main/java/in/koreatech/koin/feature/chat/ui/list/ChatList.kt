package `in`.koreatech.koin.feature.chat.ui.list

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.list.component.ChatListItem
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.CHAT_ROOM_ID
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.fetchChatList()
    }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(id = R.string.chat_list_title),
                onNavigationIconClick = {
                    (context as Activity).finish()
                }
            )
        },
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        ChatListContent(
            chatList = uiState.chatList,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun ChatListContent(
    chatList: List<ChatListItem>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
    ) {
        items(chatList) {
            ChatListItem(
                title = it.title,
                recentMessage = it.recentMessage,
                imageUrl = it.imageUrl ?: "",
                lastMessageAt = LocalDateTime.parse(it.lastMessageAt.substring(0..26))
                    .toLocalTime(),
                unReadMessageCount = it.unReadMessageCount,
                modifier = Modifier.clickable {
                    Intent(context, ChatRoomActivity::class.java).apply {
                        putExtra(ARTICLE_ID, it.articleId)
                        putExtra(CHAT_ROOM_ID, it.chatRoomId)
                    }.also {
                        context.startActivity(it)
                    }
                }
            )
        }
    }
}
