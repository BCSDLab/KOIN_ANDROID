package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.content.Context
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatContent
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatTopBar
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatTopBarDefaults
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun GroupChatScreen(
    viewModel: GroupChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val pickMultipleMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
        if (uris.isNotEmpty()) {
            uris.forEach { uri ->
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor.use {
                    if (cursor != null && cursor.moveToFirst()) {
                        val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                        if (fileNameIndex != -1 && fileSizeIndex != -1) {
                            val fileName = cursor.getString(fileNameIndex)
                            val fileSize = cursor.getLong(fileSizeIndex)
                            val fileType = context.contentResolver.getType(uri) ?: "image/${fileName.split(".").last()}"

                            viewModel.uploadImage(fileSize, fileType, fileName, uri)
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
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
            navigateBack = {
                onBackPressedDispatcher?.onBackPressed()
            }
        )
    }

    GroupChatScaffold(
        departure = uiState.departure,
        arrival = uiState.arrival,
        departureTime = uiState.departureTime,
        currentMemberCount = uiState.currentMemberCount,
        maxMemberCount = uiState.maxMemberCount,
        isLoading = uiState.isLoading,
        messages = uiState.messages,
        memberColors = uiState.memberColors,
        chatInputValue = uiState.chatInputValue,
        uploadingImage = uiState.uploadingImage,
        showImage = Pair(uiState.showImage.first, uiState.showImage.second.toUri()),
        onNavigationIconClick = { onBackPressedDispatcher?.onBackPressed() },
        onChatInputValueChange = viewModel::onChatInputValueChange,
        onImageButtonClick = {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onSendClick = viewModel::sendMessage,
        onShowImageChange = viewModel::changeShowImageState
    )
}

@Composable
private fun GroupChatScaffold(
    departure: String,
    arrival: String,
    departureTime: String,
    currentMemberCount: Int,
    maxMemberCount: Int,
    isLoading: Boolean,
    messages: ImmutableList<GroupChatMessageGroup>,
    memberColors: ImmutableMap<Int, Int>,
    chatInputValue: String,
    uploadingImage: ImmutableList<`in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage>,
    showImage: Pair<Boolean, android.net.Uri>,
    onNavigationIconClick: () -> Unit,
    onChatInputValueChange: (String) -> Unit = {},
    onImageButtonClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onShowImageChange: (Boolean, android.net.Uri) -> Unit = { _, _ -> }
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            GroupChatTopBar(
                departure = departure,
                arrival = arrival,
                departureTime = departureTime,
                currentMemberCount = currentMemberCount,
                maxMemberCount = maxMemberCount,
                onNavigationIconClick = onNavigationIconClick,
                colors = GroupChatTopBarDefaults.purpleColors()
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        containerColor = RebrandKoinTheme.colors.neutral0
    ) { contentPadding ->
        GroupChatContent(
            modifier = Modifier.padding(contentPadding),
            isLoading = isLoading,
            messages = messages,
            memberColors = memberColors,
            chatInputValue = chatInputValue,
            uploadingImage = uploadingImage,
            showImage = showImage,
            onChatInputValueChange = onChatInputValueChange,
            onImageButtonClick = onImageButtonClick,
            onSendClick = onSendClick,
            onShowImageChange = onShowImageChange
        )
    }
}

private fun handleSideEffect(
    sideEffect: GroupChatSideEffect,
    context: Context,
    navigateBack: () -> Unit
) {
    when (sideEffect) {
        is GroupChatSideEffect.NavigateBack -> navigateBack()

        is GroupChatSideEffect.FailedToLoadMessages -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_load_messages),
                Toast.LENGTH_SHORT
            ).show()
        }

        is GroupChatSideEffect.FailedToSendMessage -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_send_message),
                Toast.LENGTH_SHORT
            ).show()
        }

        is GroupChatSideEffect.FailedToUploadImage -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_upload_image),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupChatScreenPreview() {
    GroupChatScaffold(
        departure = PREVIEW_DEPARTURE,
        arrival = PREVIEW_ARRIVAL,
        departureTime = PREVIEW_DEPARTURE_TIME,
        currentMemberCount = PREVIEW_CURRENT_MEMBER_COUNT,
        maxMemberCount = PREVIEW_MAX_MEMBER_COUNT,
        isLoading = false,
        messages = previewMessages(),
        memberColors = persistentMapOf(0 to 0, 1 to 4, 2 to 2, 3 to 6),
        chatInputValue = "",
        uploadingImage = persistentListOf(),
        showImage = Pair(false, android.net.Uri.EMPTY),
        onNavigationIconClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupChatScreenEmptyPreview() {
    GroupChatScaffold(
        departure = PREVIEW_DEPARTURE,
        arrival = PREVIEW_ARRIVAL,
        departureTime = PREVIEW_DEPARTURE_TIME,
        currentMemberCount = PREVIEW_CURRENT_MEMBER_COUNT,
        maxMemberCount = PREVIEW_MAX_MEMBER_COUNT,
        isLoading = false,
        messages = persistentListOf(),
        memberColors = persistentMapOf(),
        chatInputValue = "",
        uploadingImage = persistentListOf(),
        showImage = Pair(false, android.net.Uri.EMPTY),
        onNavigationIconClick = {}
    )
}

private fun previewMessages(): ImmutableList<GroupChatMessageGroup> = persistentListOf(
    GroupChatMessageGroup(
        date = "2026년 1월 2일",
        messages = persistentListOf(
            GroupChatMessage(
                id = "msg_1",
                userId = 0,
                userNickname = "신짱구",
                content = "넵!!",
                timestamp = "13:53",
                isSentByMe = false,
                readCount = 6,
                isFirstInGroup = true
            ),
            GroupChatMessage(
                id = "msg_2",
                userId = 0,
                userNickname = "신짱구",
                content = "방송국 건너편인가요?",
                timestamp = "14:53",
                isSentByMe = false,
                readCount = 6,
                isFirstInGroup = false
            ),
            GroupChatMessage(
                id = "msg_3",
                userId = 1,
                userNickname = "나",
                content = "네 맞습니다",
                timestamp = "15:53",
                isSentByMe = true,
                readCount = 6
            ),
            GroupChatMessage(
                id = "msg_4",
                userId = 2,
                userNickname = "이훈이",
                content = "5분 정도 늦을 것 같아요 ㅠㅠ",
                timestamp = "14:53",
                isSentByMe = false,
                readCount = 6,
                isFirstInGroup = true
            )
        )
    )
)

private const val PREVIEW_DEPARTURE: String = "테니스장"
private const val PREVIEW_ARRIVAL: String = "천안터미널"
private const val PREVIEW_DEPARTURE_TIME: String = "16:00"
private const val PREVIEW_CURRENT_MEMBER_COUNT: Int = 6
private const val PREVIEW_MAX_MEMBER_COUNT: Int = 8
