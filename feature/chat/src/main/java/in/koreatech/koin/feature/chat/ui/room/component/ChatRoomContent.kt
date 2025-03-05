package `in`.koreatech.koin.feature.chat.ui.room.component

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun ChatRoomContent(
    messages: List<Pair<LocalDate, List<ConvertedChatMessage>>>,
    uploadingImage: List<ConvertedChatMessage>,
    chatPartnerProfileImage: Uri?,
    chatInputValue: String,
    showBlockDialog: Boolean,
    showImage: Pair<Boolean, Uri>,
    onBlockUser: () -> Unit,
    onBlockCancel: () -> Unit,
    onChatInputValueChange: (String) -> Unit,
    onImageButtonClick: () -> Unit,
    onSendClick: () -> Unit,
    onShowImageChange: (Boolean, Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (showImage.first) {
        BackHandler {
            onShowImageChange(false, Uri.EMPTY)
        }
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(KoinTheme.colors.neutral800),
            contentAlignment = Alignment.Center,
        ) {
            SubcomposeAsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(showImage.second)
                        .crossfade(true)
                        .build(),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
        }
    } else {
        LaunchedEffect(messages) {
            scope.launch {
                scrollState.scrollToItem(0)
            }
        }

        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(KoinTheme.colors.neutral0)
                        .weight(1f),
                state = scrollState,
                reverseLayout = true,
                verticalArrangement = Arrangement.Top,
            ) {
                messages.asReversed().forEach { (date, messages) ->
                    // Render the message before date because we are using reverse layout
                    items(
                        items = messages.plus(uploadingImage).asReversed(),
                        key = { message -> "${message.userId}:${message.content}:${message.timestamp}" },
                    ) { message ->
                        ChatBubble(
                            message = message,
                            chatPartnerProfileImage = chatPartnerProfileImage,
                            onShowImageChange = onShowImageChange,
                        )
                    }
                    item {
                        ChatDateTitle(date)
                    }
                }
            }
            ChatInput(
                value = chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageButtonClick = onImageButtonClick,
                onSendClick = onSendClick,
            )
            if (showBlockDialog) {
                ChoiceDialog(
                    title = stringResource(id = R.string.block_dialog_title),
                    description = stringResource(id = R.string.block_dialog_description),
                    onPositive = onBlockUser,
                    onNegative = onBlockCancel,
                )
            }
        }
    }
}
