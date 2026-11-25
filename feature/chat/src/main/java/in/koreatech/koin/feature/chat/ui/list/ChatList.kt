package `in`.koreatech.koin.feature.chat.ui.list

import android.app.Activity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import `in`.koreatech.koin.core.analytics.ChatAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventCategory
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.component.ChatProgressIndicator
import `in`.koreatech.koin.feature.chat.ui.component.ChatTopBar
import `in`.koreatech.koin.feature.chat.ui.list.component.ChatListItem
import java.time.LocalDateTime
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatList(
    navigateToChatRoom: (Int, Int) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel(),
    showBlockedMessage: Boolean = false
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val blockedMessage = stringResource(R.string.block_snackbar_message)
    var hasShownBlockedMessage by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigator = rememberNavigator()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ChatListSideEffect.NavigateToLogin -> {
                context.startActivity(navigator.navigateToSignIn(context))
                (context as? Activity)?.finish()
            }
        }
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.startChatListUpdates()
            try {
                awaitCancellation()
            } finally {
                viewModel.stopChatListUpdates()
            }
        }
    }

    LaunchedEffect(showBlockedMessage, blockedMessage) {
        if (showBlockedMessage && !hasShownBlockedMessage) {
            hasShownBlockedMessage = true
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = blockedMessage
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        containerColor = RebrandKoinTheme.colors.neutral800,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        shape = RebrandKoinTheme.shapes.small
                    )
                }
            )
        },
        topBar = {
            ChatTopBar(
                title = stringResource(id = R.string.chat_list_title),
                onNavigationIconClick = { (context as Activity).finish() }
            )
        },
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { contentPadding ->
        ChatListContent(
            isLoading = uiState.isLoading,
            chatList = uiState.chatList,
            navigateToChatRoom = { articleId, chatRoomId ->
                EventLogger.logCustomEvent(
                    ChatAnalyticsConstant.EVENT_NAME,
                    EventCategory.CLICK.value,
                    ChatAnalyticsConstant.Label.CHAT_LIST_CONVERSATION_ENTRY,
                    ChatAnalyticsConstant.Value.MESSAGE
                )
                navigateToChatRoom(articleId, chatRoomId)
            },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun ChatListContent(
    isLoading: Boolean,
    chatList: List<ChatListItem>,
    navigateToChatRoom: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        ChatProgressIndicator()
        return
    }
    LazyColumn(
        modifier = modifier
    ) {
        items(chatList) {
            ChatListItem(
                title = it.title,
                recentMessage = it.recentMessage,
                imageUrl = it.imageUrl ?: "",
                lastMessageAt =
                LocalDateTime.parse(it.lastMessageAt)
                    .toLocalTime(),
                unReadMessageCount = it.unReadMessageCount,
                modifier =
                Modifier.clickable {
                    navigateToChatRoom(it.articleId, it.chatRoomId)
                }
            )
        }
    }
}
