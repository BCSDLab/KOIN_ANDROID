package `in`.koreatech.koin.feature.chat.ui.chat

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.component.input.KoinChatInput
import `in`.koreatech.koin.core.designsystem.component.input.KoinChatInputDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.component.ChatDateChip
import `in`.koreatech.koin.feature.chat.ui.component.ChatImage
import `in`.koreatech.koin.feature.chat.ui.component.ChatMessageBubble
import `in`.koreatech.koin.feature.chat.ui.component.ChatProgressIndicator
import `in`.koreatech.koin.feature.chat.ui.component.ChatReadObserver
import `in`.koreatech.koin.feature.chat.ui.component.ChatReadViewModel
import `in`.koreatech.koin.feature.chat.ui.component.ChatTopBar
import `in`.koreatech.koin.feature.chat.ui.component.ChatUserIcon
import `in`.koreatech.koin.feature.chat.util.handleSelectedImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChatScreen(
    navigateToChatList: (Boolean) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatReadViewModel: ChatReadViewModel = hiltViewModel()
    val context = LocalContext.current
    val state by viewModel.collectAsState()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
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
    viewModel.collectSideEffect { effect ->
        handleSideEffect(effect, context, navigateToChatList) { backDispatcher?.onBackPressed() }
    }
    ChatScreenImpl(
        state = state,
        onNavigationIconClick = { backDispatcher?.onBackPressed() },
        onChatInputValueChange = viewModel::onChatInputValueChange,
        uploadImage = viewModel::uploadImage,
        onSendClick = viewModel::sendMessage,
        onMenuClick = { viewModel.updateMenu(showMenu = !state.showMenu) },
        onMenuDismiss = { viewModel.updateMenu(showMenu = false) },
        onBlockClick = { viewModel.updateMenu(showMenu = false, showBlockDialog = true) },
        onBlockDialogDismiss = { viewModel.updateMenu(showBlockDialog = false) },
        onBlockUser = viewModel::blockUser,
        onReadMessage = chatReadViewModel::markRead
    )
}

@Composable
private fun ChatScreenImpl(
    state: ChatState,
    onNavigationIconClick: () -> Unit,
    onChatInputValueChange: (String) -> Unit,
    uploadImage: (Long, String, String, Uri) -> Unit,
    onSendClick: () -> Unit,
    onMenuClick: () -> Unit,
    onMenuDismiss: () -> Unit,
    onBlockClick: () -> Unit,
    onBlockDialogDismiss: () -> Unit,
    onBlockUser: () -> Unit,
    onReadMessage: suspend (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val latestMessageId = state.messages.lastOrNull()?.messages?.lastOrNull()?.id
    var expandedImageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    val onImageClick: (Uri) -> Unit = remember { { uri -> expandedImageUrl = uri.toString() } }
    val partnerProfileImage = remember(state.profileImage) { state.profileImage?.let(Uri::parse) }

    LaunchedEffect(latestMessageId, state.isUploadingImage) {
        if ((latestMessageId != null || state.isUploadingImage) && scrollState.firstVisibleItemIndex < 3) {
            scrollState.animateScrollToItem(0)
        }
    }
    if (!state.isLoading && expandedImageUrl == null) {
        ChatReadObserver(scrollState, latestMessageId, state.roomId.takeIf { state.supportsReadReceipts }, onReadMessage)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            ChatTopBar(
                title = state.title,
                onNavigationIconClick = onNavigationIconClick,
                actions = {
                    if (state.canBlockUser) {
                        IconButton(onClick = onMenuClick) {
                            Icon(Icons.Default.MoreVert, stringResource(R.string.chat_toolbar_more))
                        }
                        DropdownMenu(
                            expanded = state.showMenu,
                            onDismissRequest = onMenuDismiss,
                            shape = RebrandKoinTheme.shapes.medium,
                            containerColor = RebrandKoinTheme.colors.neutral50
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.chat_block_user), style = RebrandKoinTheme.typography.medium14) },
                                trailingIcon = { Icon(painterResource(R.drawable.ic_chat_block_user), stringResource(R.string.chat_block_user)) },
                                onClick = onBlockClick
                            )
                        }
                    }
                }
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
                enabled = !state.isLoading && !state.isUploadingImage && state.canSend,
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
                            authorNickname = message.authorNickname.takeUnless { message.isSentByMe },
                            avatar = { ChatUserIcon(imageUri = partnerProfileImage) },
                            isLeftUser = message.isLeftUser,
                            onImageClick = onImageClick
                        )
                    }
                    item(key = "date-${group.date}") { ChatDateChip(date = group.date) }
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
    if (state.showBlockDialog) {
        AlertDialog(
            onDismissRequest = onBlockDialogDismiss,
            title = { Text(stringResource(R.string.block_dialog_title), style = RebrandKoinTheme.typography.medium18) },
            text = { Text(stringResource(R.string.block_dialog_description), style = RebrandKoinTheme.typography.regular14) },
            confirmButton = {
                TextButton(onClick = {
                    onBlockUser()
                    onBlockDialogDismiss()
                }) {
                    Text(
                        stringResource(`in`.koreatech.koin.core.designsystem.R.string.common_confirmation),
                        style = RebrandKoinTheme.typography.medium14,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onBlockDialogDismiss) {
                    Text(
                        stringResource(`in`.koreatech.koin.core.designsystem.R.string.common_cancellation),
                        style = RebrandKoinTheme.typography.medium14,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
            },
            containerColor = RebrandKoinTheme.colors.neutral0,
            shape = RebrandKoinTheme.shapes.small
        )
    }
}

private fun handleSideEffect(
    effect: ChatSideEffect,
    context: Context,
    navigateToChatList: (Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    when (effect) {
        ChatSideEffect.FailedToConnect -> toast(context, R.string.failed_to_connect_to_server)
        ChatSideEffect.FailedToCreateRoom -> toast(context, R.string.failed_to_load_chat_room)
        ChatSideEffect.FailedToLoadMessages -> toast(context, R.string.failed_to_load_messages)
        ChatSideEffect.FailedToSendMessage -> toast(context, R.string.failed_to_send_message)
        ChatSideEffect.FailedToUploadImage -> toast(context, R.string.failed_to_upload_image)
        ChatSideEffect.DirectChatUnavailable -> {
            toast(context, R.string.chat_unavailable)
            navigateBack()
        }
        ChatSideEffect.BlockUserFailed -> toast(context, R.string.failed_to_block_user)
        ChatSideEffect.BlockUserSuccess -> navigateToChatList(true)
        ChatSideEffect.BlockedByUser -> {
            toast(context, R.string.blocked_by_user)
            navigateBack()
        }
    }
}

private fun toast(context: Context, message: Int) {
    Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
}
