package `in`.koreatech.koin.feature.chat.ui.list

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.list.component.ChatListItem
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomActivity
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.CHAT_ROOM_ID
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    viewModel: ChatListViewModel = hiltViewModel(),
    showBlockedMessage: Boolean = false
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showBlockedMessage) {
        if (showBlockedMessage) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.block_snackbar_message),
                )
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.fetchChatList()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        containerColor = Color(0xCC041A44),
                        contentColor = KoinTheme.colors.neutral0,
                        shape = KoinTheme.shapes.small
                    )
                }
            )
        },
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
            navigateToChatRoom = { articleId, chatRoomId ->
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.CHAT.MESSAGE_LIST_SELECT,
                    "쪽지"
                )
                Intent(context, ChatRoomActivity::class.java).apply {
                    putExtra(ARTICLE_ID, articleId)
                    putExtra(CHAT_ROOM_ID, chatRoomId)
                }.let(context::startActivity)
            },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun ChatListContent(
    chatList: List<ChatListItem>,
    navigateToChatRoom: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(chatList) {
            ChatListItem(
                title = it.title,
                recentMessage = it.recentMessage,
                imageUrl = it.imageUrl ?: "",
                lastMessageAt = LocalDateTime.parse(it.lastMessageAt)
                    .toLocalTime(),
                unReadMessageCount = it.unReadMessageCount,
                modifier = Modifier.clickable {
                    navigateToChatRoom(it.articleId, it.chatRoomId)
                }
            )
        }
    }
}
