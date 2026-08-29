package `in`.koreatech.koin.feature.recruitment.ui.chat.groupchat

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoomStatus
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatDateChip
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatInput
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatMessageBubble
import `in`.koreatech.koin.feature.recruitment.ui.chat.components.RecruitmentChatTopBar
import `in`.koreatech.koin.feature.recruitment.ui.chat.model.RecruitmentChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RecruitmentGroupChatScreen(
    viewModel: RecruitmentGroupChatViewModel = hiltViewModel()
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
        handleSideEffect(sideEffect = it, context = context)
    }

    RecruitmentGroupChatScreenImpl(
        title = uiState.title,
        currentMemberCount = uiState.currentMemberCount,
        maxMemberCount = uiState.maxMemberCount,
        isReadOnly = uiState.status == RecruitmentChatRoomStatus.READ_ONLY,
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
private fun RecruitmentGroupChatScreenImpl(
    title: String,
    currentMemberCount: Int,
    maxMemberCount: Int,
    isReadOnly: Boolean,
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
                title = title,
                onNavigationIconClick = onNavigationIconClick,
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_recruitment_chat_person_count),
                            contentDescription = stringResource(id = R.string.recruitment_chat_member_count_icon),
                            modifier = Modifier.size(16.dp),
                            tint = RebrandKoinTheme.colors.neutral600
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                R.string.recruitment_chat_toolbar_participants,
                                currentMemberCount,
                                maxMemberCount
                            ),
                            style = RebrandKoinTheme.typography.regular12,
                            color = RebrandKoinTheme.colors.neutral600
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
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
                                authorNickname = if (message.isFirstInGroup) message.authorNickname else null
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
                            .fillMaxSize()
                            .background(RebrandKoinTheme.colors.neutral0),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            RecruitmentChatInput(
                value = chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageButtonClick = onImageButtonClick,
                onSendClick = onSendClick,
                enabled = !isReadOnly && !isUploadingImage
            )
        }
    }
}

private fun handleSideEffect(
    sideEffect: RecruitmentGroupChatSideEffect,
    context: Context
) {
    val messageRes = when (sideEffect) {
        RecruitmentGroupChatSideEffect.FailedToLoadChatRoom -> R.string.recruitment_chat_failed_to_load_chat_room
        RecruitmentGroupChatSideEffect.FailedToLoadMessages -> R.string.recruitment_chat_failed_to_load_messages
        RecruitmentGroupChatSideEffect.FailedToSendMessage -> R.string.recruitment_chat_failed_to_send_message
        RecruitmentGroupChatSideEffect.FailedToUploadImage -> R.string.recruitment_chat_failed_to_upload_image
        RecruitmentGroupChatSideEffect.ChatRoomReadOnly -> R.string.recruitment_chat_read_only
        RecruitmentGroupChatSideEffect.MessageTooFast -> R.string.recruitment_chat_message_too_fast
    }
    Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
}

private fun handleSelectedImages(
    uris: List<Uri>,
    context: Context,
    uploadImage: (Long, String, String, Uri) -> Unit
) {
    uris.forEach { uri ->
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use

            val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            if (fileNameIndex == -1 || fileSizeIndex == -1) return@use

            val fileName = cursor.getString(fileNameIndex)
            val fileSize = cursor.getLong(fileSizeIndex)
            val fileType = context.contentResolver.getType(uri) ?: "image/${fileName.substringAfterLast(".")}"

            uploadImage(fileSize, fileType, fileName, uri)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentGroupChatScreenPreview() {
    RebrandKoinTheme {
        RecruitmentGroupChatScreenImpl(
            title = RecruitmentGroupChatPreviewData.TITLE,
            currentMemberCount = RecruitmentGroupChatPreviewData.CURRENT_MEMBER_COUNT,
            maxMemberCount = RecruitmentGroupChatPreviewData.MAX_MEMBER_COUNT,
            isReadOnly = false,
            isLoading = false,
            isUploadingImage = false,
            messages = RecruitmentGroupChatPreviewData.messages(),
            chatInputValue = ""
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentGroupChatScreenEmptyPreview() {
    RebrandKoinTheme {
        RecruitmentGroupChatScreenImpl(
            title = RecruitmentGroupChatPreviewData.TITLE,
            currentMemberCount = RecruitmentGroupChatPreviewData.CURRENT_MEMBER_COUNT,
            maxMemberCount = RecruitmentGroupChatPreviewData.MAX_MEMBER_COUNT,
            isReadOnly = false,
            isLoading = false,
            isUploadingImage = false,
            messages = persistentListOf(),
            chatInputValue = ""
        )
    }
}
