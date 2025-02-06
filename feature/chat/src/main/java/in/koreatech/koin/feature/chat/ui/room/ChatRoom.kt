package `in`.koreatech.koin.feature.chat.ui.room

import android.app.Activity
import android.content.Context
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.room.ChatRoomViewModel.Companion.ARTICLE_ID
import `in`.koreatech.koin.feature.chat.ui.room.component.ChatRoomContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoom(
    articleId: Int,
) {
    val savedStateHandle = SavedStateHandle()
    savedStateHandle[ARTICLE_ID] = articleId

    val viewModelStoreOwner = requireNotNull(
        LocalViewModelStoreOwner.current as? HasDefaultViewModelProviderFactory
    )
    val viewModel: ChatRoomViewModel = viewModel(
        extras = viewModelStoreOwner.defaultViewModelCreationExtras.withCreationCallback<ChatRoomViewModel.Factory> {
            it.create(savedStateHandle = savedStateHandle)
        }
    )
    val context = LocalContext.current

    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
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
                                val fileType = context.contentResolver.getType(uri)
                                    ?: "image/${fileName.split(".").last()}"

                                viewModel.getPreSignedUrl(
                                    fileSize,
                                    fileType,
                                    fileName,
                                    uri
                                )
                            }
                        }
                    }
                }
            }
        }

    viewModel.collectSideEffect {
        handleSideEffect(it, context)
    }

    val uiState by viewModel.collectAsState()
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            KoinTopAppBar(
                title = uiState.articleTitle,
                actions = {
                    IconButton(onClick = {
                        viewModel.changeMenuState(!uiState.showMenu)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.chat_toolbar_more)
                        )
                    }

                    DropdownMenu(
                        modifier = Modifier,
                        expanded = uiState.showMenu,
                        onDismissRequest = { viewModel.changeMenuState(false) },
                        shape = KoinTheme.shapes.medium,
                        containerColor = KoinTheme.colors.neutral50,
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(id = R.string.chat_block_user),
                                    style = KoinTheme.typography.medium14,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chat_block_user),
                                    contentDescription = stringResource(id = R.string.chat_block_user)
                                )
                            },
                            onClick = {
                                viewModel.changeBlockDialogState(true)
                            }
                        )
                    }
                },
            )

        },
        // Handle the navigation bar and IME insets on ChatInput
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime)
    ) { contentPadding ->
        ChatRoomContent(
            modifier = Modifier
                .padding(contentPadding),
            messages = uiState.chatMessage,
            chatPartnerProfileImage = uiState.chatPartnerProfileImage,
            chatInputValue = uiState.chatInputValue,
            showBlockDialog = uiState.showBlockDialog,
            onBlockUser = {
                viewModel.blockUser()
                viewModel.changeBlockDialogState(false)
            },
            onBlockCancel = { viewModel.changeBlockDialogState(false) },
            onChatInputValueChange = { viewModel.onChatInputValueChange(it) },
            onImageButtonClick = {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onSendClick = { viewModel.sendMessage() }
        )
    }
}

fun handleSideEffect(
    sideEffect: ChatRoomSideEffect,
    context: Context
) {
    when (sideEffect) {
        ChatRoomSideEffect.FailedToConnectWS -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_connect_to_server),
                Toast.LENGTH_SHORT
            ).show()
        }

        ChatRoomSideEffect.FailedToUploadImage -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_upload_image),
                Toast.LENGTH_SHORT
            ).show()
        }

        ChatRoomSideEffect.BlockUserFailed -> {
            Toast.makeText(
                context,
                context.getString(R.string.failed_to_block_user),
                Toast.LENGTH_SHORT
            ).show()
        }

        ChatRoomSideEffect.BlockUserSuccess -> {
            (context as Activity).finish()
        }
    }
}
