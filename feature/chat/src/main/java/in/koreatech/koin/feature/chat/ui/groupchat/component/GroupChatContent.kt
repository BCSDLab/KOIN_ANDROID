package `in`.koreatech.koin.feature.chat.ui.groupchat.component

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.component.ChatProgressIndicator
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessageGroup
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import `in`.koreatech.koin.feature.chat.ui.room.component.ChatDateTitle
import `in`.koreatech.koin.feature.chat.ui.room.component.ChatDateTitleDefaults
import `in`.koreatech.koin.feature.chat.ui.room.component.ChatInput
import `in`.koreatech.koin.feature.chat.ui.room.component.ChatInputDefaults
import `in`.koreatech.koin.feature.chat.util.parseDateString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun GroupChatContent(
    isLoading: Boolean,
    messages: ImmutableList<GroupChatMessageGroup>,
    memberColors: ImmutableMap<Int, Int>,
    chatInputValue: String,
    uploadingImage: ImmutableList<ConvertedChatMessage>,
    showImage: Pair<Boolean, Uri>,
    modifier: Modifier = Modifier,
    onChatInputValueChange: (String) -> Unit = {},
    onImageButtonClick: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    val scrollState = rememberLazyListState()
    rememberCoroutineScope()

    if (isLoading) {
        ChatProgressIndicator()
        return
    }

    if (showImage.first) {
        BackHandler {
            onShowImageChange(false, Uri.EMPTY)
        }
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(showImage.second)
                    .crossfade(true)
                    .build(),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Fit,
                contentDescription = stringResource(id = R.string.group_chat_fullscreen_image)
            )
        }
    } else {
        LaunchedEffect(messages) {
            if (messages.isNotEmpty()) {
                scrollState.scrollToItem(0)
            }
        }

        Column(
            modifier = modifier.fillMaxSize()
        ) {
            val reversedMessages = messages.asReversed()
            val lastDate = reversedMessages.firstOrNull()?.date

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RebrandKoinTheme.colors.neutral0)
                    .weight(1f),
                state = scrollState,
                reverseLayout = true,
                verticalArrangement = Arrangement.Top
            ) {
                reversedMessages.forEach { (date, dateMessages) ->
                    items(
                        items = dateMessages.asReversed(),
                        key = { message -> message.id }
                    ) { message ->
                        GroupChatMessageBubble(
                            message = message,
                            userColorIndex = memberColors[message.userId] ?: 0,
                            onShowImageChange = onShowImageChange,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    if (uploadingImage.isNotEmpty() && lastDate == date) {
                        items(
                            items = uploadingImage,
                            key = { uploading -> "uploading_${uploading.timestamp}" }
                        ) { uploading ->
                            val uploadingMessage = GroupChatMessage(
                                id = "uploading_${uploading.timestamp}",
                                userId = uploading.userId,
                                userNickname = uploading.userNickname,
                                content = uploading.content,
                                timestamp = uploading.timestamp.toLocalTime().toString().substring(0, 5),
                                isImage = true,
                                isSentByMe = uploading.isSentByMe,
                                readCount = 0
                            )
                            GroupChatMessageBubble(
                                message = uploadingMessage,
                                userColorIndex = memberColors[uploading.userId] ?: 0,
                                onShowImageChange = onShowImageChange,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                    item(key = "date_$date") {
                        val localDate = parseDateString(date)
                        ChatDateTitle(
                            date = localDate,
                            colors = ChatDateTitleDefaults.purpleColors()
                        )
                    }
                }
            }

            ChatInput(
                value = chatInputValue,
                onValueChange = onChatInputValueChange,
                onImageButtonClick = onImageButtonClick,
                onSendClick = onSendClick,
                colors = ChatInputDefaults.purpleColors()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupChatContentPreview() {
    GroupChatContent(
        isLoading = false,
        messages = persistentListOf(
            GroupChatMessageGroup(
                date = "2026년 1월 2일",
                messages = persistentListOf(
                    GroupChatMessage(
                        id = "1",
                        userId = 0,
                        userNickname = "신짱구",
                        content = "넵!!",
                        timestamp = "13:53",
                        isSentByMe = false,
                        readCount = 6,
                        isFirstInGroup = true
                    ),
                    GroupChatMessage(
                        id = "2",
                        userId = 0,
                        userNickname = "신짱구",
                        content = "방송국 건너편인가요?",
                        timestamp = "14:53",
                        isSentByMe = false,
                        readCount = 6,
                        isFirstInGroup = false
                    ),
                    GroupChatMessage(
                        id = "3",
                        userId = 1,
                        userNickname = "나",
                        content = "네 맞습니다",
                        timestamp = "15:53",
                        isSentByMe = true,
                        readCount = 6
                    )
                )
            )
        ),
        memberColors = persistentMapOf(0 to 0, 1 to 1),
        chatInputValue = "",
        uploadingImage = persistentListOf(),
        showImage = Pair(false, Uri.EMPTY)
    )
}
