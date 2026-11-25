package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.analytics.CallvanAnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventCategory
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.input.KoinChatInput
import `in`.koreatech.koin.core.designsystem.component.input.KoinChatInputDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.chat.ChatDomainType
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.component.ChatDateChip
import `in`.koreatech.koin.feature.chat.ui.component.ChatImage
import `in`.koreatech.koin.feature.chat.ui.component.ChatMessageBubble
import `in`.koreatech.koin.feature.chat.ui.component.ChatProgressIndicator
import `in`.koreatech.koin.feature.chat.ui.component.ChatReadObserver
import `in`.koreatech.koin.feature.chat.ui.component.ChatReadViewModel
import `in`.koreatech.koin.feature.chat.ui.component.ChatTopBar
import `in`.koreatech.koin.feature.chat.util.handleSelectedImages
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GroupChatScreen(
    viewModel: GroupChatViewModel = hiltViewModel()
) {
    val chatReadViewModel: ChatReadViewModel = hiltViewModel()
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.startReceivingMessages()
                Lifecycle.Event.ON_PAUSE -> viewModel.stopReceivingMessages()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.stopReceivingMessages()
        }
    }

    viewModel.collectSideEffect { handleSideEffect(it, context) }
    GroupChatScreenImpl(
        state = state,
        onNavigationIconClick = { onBackPressedDispatcher?.onBackPressed() },
        onChatInputValueChange = viewModel::onChatInputValueChange,
        uploadImage = viewModel::uploadImage,
        onSendClick = {
            if (state.domainType == ChatDomainType.CALLVAN) {
                EventLogger.logCustomEvent(
                    CallvanAnalyticsConstant.EVENT_NAME,
                    EventCategory.CLICK.value,
                    CallvanAnalyticsConstant.Label.CHAT_MESSAGE_SEND,
                    ""
                )
            }
            viewModel.sendMessage()
        },
        onReadMessage = chatReadViewModel::markRead
    )
}

@Composable
private fun GroupChatScreenImpl(
    state: GroupChatState,
    onNavigationIconClick: () -> Unit,
    onChatInputValueChange: (String) -> Unit,
    uploadImage: (Long, String, String, Uri) -> Unit,
    onSendClick: () -> Unit,
    onReadMessage: suspend (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val latestMessageId = state.messages.lastOrNull()?.messages?.lastOrNull()?.id
    val isUploadingImage = state.uploadingImages.isNotEmpty()
    var expandedImageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    val onImageClick: (Uri) -> Unit = remember { { uri -> expandedImageUrl = uri.toString() } }
    val screenTitle = when (state.domainType) {
        ChatDomainType.CALLVAN -> stringResource(
            R.string.chat_callvan_title,
            state.departure,
            state.arrival,
            state.departureTime
        )
        else -> state.title
    }

    LaunchedEffect(latestMessageId, isUploadingImage) {
        if ((latestMessageId != null || isUploadingImage) && scrollState.firstVisibleItemIndex < 3) {
            scrollState.animateScrollToItem(0)
        }
    }
    if (!state.isLoading && expandedImageUrl == null) {
        ChatReadObserver(scrollState, latestMessageId, state.readReceiptRoomId, onReadMessage)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            ChatTopBar(
                title = screenTitle,
                onNavigationIconClick = onNavigationIconClick,
                memberCount = state.currentMemberCount,
                maxMemberCount = state.maxMemberCount
            )
        },
        bottomBar = {
            KoinChatInput(
                value = state.chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageSelected = { uris ->
                    if (uris.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            handleSelectedImages(uris, context, uploadImage)
                        }
                    }
                },
                onSendClick = onSendClick,
                enabled = !state.isLoading && !isUploadingImage && state.canSend,
                colors = KoinChatInputDefaults.purpleColors()
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                reverseLayout = true
            ) {
                items(state.uploadingImages.asReversed(), key = { it.id }) { message ->
                    ChatMessageBubble(
                        content = message.content,
                        timestamp = message.timestamp,
                        isSentByMe = true,
                        isImage = true,
                        isUploading = true,
                        onImageClick = onImageClick
                    )
                }
                state.messages.asReversed().forEach { group ->
                    items(group.messages.asReversed(), key = { it.id }) { message ->
                        ChatMessageBubble(
                            content = message.content,
                            timestamp = message.timestamp,
                            isSentByMe = message.isSentByMe,
                            isImage = message.isImage,
                            authorNickname = message.authorNickname.takeIf { message.isFirstInGroup },
                            isLeftUser = message.isLeftUser,
                            onImageClick = onImageClick
                        )
                    }
                    item(key = "date_${group.date}") {
                        ChatDateChip(group.date)
                    }
                }
            }
            if (state.isLoading) {
                ChatProgressIndicator(Modifier.background(RebrandKoinTheme.colors.neutral0))
            }
        }
    }

    expandedImageUrl?.let { imageUrl ->
        Dialog(
            onDismissRequest = { expandedImageUrl = null },
            properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(RebrandKoinTheme.colors.neutral0),
                contentAlignment = Alignment.Center
            ) {
                ChatImage(imageUrl = imageUrl, modifier = Modifier.fillMaxSize())
                IconButton(
                    onClick = { expandedImageUrl = null },
                    modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.chat_close_image),
                        tint = RebrandKoinTheme.colors.neutral800
                    )
                }
            }
        }
    }
}

private fun handleSideEffect(sideEffect: GroupChatSideEffect, context: Context) {
    val messageRes = when (sideEffect) {
        GroupChatSideEffect.FailedToLoadChatRoom -> R.string.failed_to_load_chat_room
        GroupChatSideEffect.FailedToLoadMessages -> R.string.failed_to_load_messages
        GroupChatSideEffect.FailedToSendMessage -> R.string.failed_to_send_message
        GroupChatSideEffect.FailedToUploadImage -> R.string.failed_to_upload_image
    }
    Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
private fun GroupChatScreenPreview() {
    RebrandKoinTheme {
        GroupChatScreenImpl(
            state = GroupChatState(
                domainType = ChatDomainType.CALLVAN,
                departure = GroupChatPreviewData.DEPARTURE,
                arrival = GroupChatPreviewData.ARRIVAL,
                departureTime = GroupChatPreviewData.DEPARTURE_TIME,
                currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
                maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT,
                isLoading = false,
                messages = GroupChatPreviewData.messages()
            ),
            onNavigationIconClick = {},
            onChatInputValueChange = {},
            uploadImage = { _, _, _, _ -> },
            onSendClick = {},
            onReadMessage = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupChatScreenEmptyPreview() {
    RebrandKoinTheme {
        GroupChatScreenImpl(
            state = GroupChatState(
                domainType = ChatDomainType.TEAM_RECRUITMENT,
                title = "모집 채팅방",
                currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
                maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT,
                isLoading = false,
                messages = persistentListOf()
            ),
            onNavigationIconClick = {},
            onChatInputValueChange = {},
            uploadImage = { _, _, _, _ -> },
            onSendClick = {},
            onReadMessage = { _, _ -> }
        )
    }
}
