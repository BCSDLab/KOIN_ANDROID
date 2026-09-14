package `in`.koreatech.koin.feature.recruitment.ui.chat.directchat

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatDateChip
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatInput
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatMessageBubble
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatTopBar
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatUserIcon
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import `in`.koreatech.koin.feature.recruitment.ui.chat.util.handleSelectedImages
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RecruitmentDirectChatScreen(
    viewModel: RecruitmentDirectChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val coroutineScope = rememberCoroutineScope()

    val pickMultipleMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
        if (uris.isNotEmpty()) {
            coroutineScope.launch(Dispatchers.IO) {
                handleSelectedImages(uris, context, viewModel::uploadImage)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.startPolling()
                Lifecycle.Event.ON_PAUSE -> viewModel.stopPolling()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            context = context,
            onNavigateUp = { onBackPressedDispatcher?.onBackPressed() }
        )
    }

    RecruitmentDirectChatScreenImpl(
        partnerNickname = uiState.partnerNickname,
        isLoading = uiState.isLoading,
        isUploadingImage = uiState.isUploadingImage,
        messages = uiState.messages,
        chatInputValue = uiState.chatInputValue,
        onNavigationIconClick = { onBackPressedDispatcher?.onBackPressed() },
        onChatInputValueChange = viewModel::onChatInputValueChange,
        onImageButtonClick = {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onSendClick = viewModel::sendMessage
    )
}

@Composable
private fun RecruitmentDirectChatScreenImpl(
    partnerNickname: String,
    isLoading: Boolean,
    isUploadingImage: Boolean,
    messages: ImmutableList<RecruitmentChatMessageGroup>,
    chatInputValue: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onChatInputValueChange: (String) -> Unit = {},
    onImageButtonClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    val latestMessageId = messages.lastOrNull()?.messages?.lastOrNull()?.id

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            RecruitmentChatTopBar(
                title = partnerNickname,
                onNavigationIconClick = onNavigationIconClick
            )
        },
        bottomBar = {
            RecruitmentChatInput(
                value = chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageButtonClick = onImageButtonClick,
                onSendClick = onSendClick,
                enabled = !isLoading && !isUploadingImage
            )
        },
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            LaunchedEffect(latestMessageId) {
                if (latestMessageId != null && scrollState.firstVisibleItemIndex < 3) {
                    scrollState.animateScrollToItem(0)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RebrandKoinTheme.colors.neutral0),
                state = scrollState,
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {
                messages.asReversed().forEach { group ->
                    items(
                        items = group.messages.asReversed(),
                        key = { message -> message.id }
                    ) { message ->
                        RecruitmentChatMessageBubble(
                            content = message.content,
                            timestamp = message.timestamp,
                            isSentByMe = message.isSentByMe,
                            isImage = message.isImage,
                            authorNickname = if (message.isSentByMe) null else partnerNickname,
                            avatar = { RecruitmentChatUserIcon() }
                        )
                    }
                    item(key = "date_${group.date}") {
                        RecruitmentChatDateChip(date = group.date)
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .fillMaxSize()
                        .background(RebrandKoinTheme.colors.neutral0),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

private fun handleSideEffect(
    sideEffect: RecruitmentDirectChatSideEffect,
    context: Context,
    onNavigateUp: () -> Unit
) {
    val messageRes = when (sideEffect) {
        RecruitmentDirectChatSideEffect.FailedToCreateChatRoom -> R.string.recruitment_chat_failed_to_load_chat_room
        RecruitmentDirectChatSideEffect.FailedToLoadMessages -> R.string.recruitment_chat_failed_to_load_messages
        RecruitmentDirectChatSideEffect.FailedToSendMessage -> R.string.recruitment_chat_failed_to_send_message
        RecruitmentDirectChatSideEffect.FailedToUploadImage -> R.string.recruitment_chat_failed_to_upload_image
        RecruitmentDirectChatSideEffect.MessageTooFast -> R.string.recruitment_chat_message_too_fast
        RecruitmentDirectChatSideEffect.DirectChatUnavailable -> R.string.recruitment_direct_chat_unavailable
    }
    Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
    if (sideEffect is RecruitmentDirectChatSideEffect.DirectChatUnavailable) {
        onNavigateUp()
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentDirectChatScreenPreview() {
    RebrandKoinTheme {
        RecruitmentDirectChatScreenImpl(
            partnerNickname = RecruitmentDirectChatPreviewData.PARTNER_NICKNAME,
            isLoading = false,
            isUploadingImage = false,
            messages = RecruitmentDirectChatPreviewData.messages(),
            chatInputValue = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentDirectChatScreenEmptyPreview() {
    RebrandKoinTheme {
        RecruitmentDirectChatScreenImpl(
            partnerNickname = RecruitmentDirectChatPreviewData.PARTNER_NICKNAME,
            isLoading = false,
            isUploadingImage = false,
            messages = persistentListOf(),
            chatInputValue = ""
        )
    }
}
