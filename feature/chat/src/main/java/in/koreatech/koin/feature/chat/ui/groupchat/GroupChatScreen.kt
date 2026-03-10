package `in`.koreatech.koin.feature.chat.ui.groupchat

import android.content.Context
import android.net.Uri
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatContent
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatTopBar
import `in`.koreatech.koin.feature.chat.ui.groupchat.component.GroupChatTopBarDefaults
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
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
            handleSelectedImages(uris, context, viewModel::uploadImage)
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
            context = context
        )
    }

    GroupChatScreenImpl(
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
private fun GroupChatScreenImpl(
    departure: String,
    arrival: String,
    departureTime: String,
    currentMemberCount: Int,
    maxMemberCount: Int,
    isLoading: Boolean,
    messages: ImmutableList<GroupChatMessageGroup>,
    memberColors: ImmutableMap<Int, Int>,
    chatInputValue: String,
    uploadingImage: ImmutableList<ConvertedChatMessage>,
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
    context: Context
) {
    when (sideEffect) {
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
    GroupChatScreenImpl(
        departure = GroupChatPreviewData.DEPARTURE,
        arrival = GroupChatPreviewData.ARRIVAL,
        departureTime = GroupChatPreviewData.DEPARTURE_TIME,
        currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
        maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT,
        isLoading = false,
        messages = GroupChatPreviewData.messages(),
        memberColors = GroupChatPreviewData.memberColors,
        chatInputValue = "",
        uploadingImage = persistentListOf(),
        showImage = GroupChatPreviewData.emptyImageState,
        onNavigationIconClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun GroupChatScreenEmptyPreview() {
    GroupChatScreenImpl(
        departure = GroupChatPreviewData.DEPARTURE,
        arrival = GroupChatPreviewData.ARRIVAL,
        departureTime = GroupChatPreviewData.DEPARTURE_TIME,
        currentMemberCount = GroupChatPreviewData.CURRENT_MEMBER_COUNT,
        maxMemberCount = GroupChatPreviewData.MAX_MEMBER_COUNT,
        isLoading = false,
        messages = persistentListOf(),
        memberColors = GroupChatPreviewData.emptyMemberColors,
        chatInputValue = "",
        uploadingImage = persistentListOf(),
        showImage = GroupChatPreviewData.emptyImageState,
        onNavigationIconClick = {}
    )
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
