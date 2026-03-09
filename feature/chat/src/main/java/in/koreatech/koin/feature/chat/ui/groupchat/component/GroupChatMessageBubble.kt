package `in`.koreatech.koin.feature.chat.ui.groupchat.component

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.groupchat.GroupChatPreviewData
import `in`.koreatech.koin.feature.chat.ui.groupchat.model.GroupChatMessage

object GroupChatMessageDefaults {
    @Composable
    fun purpleColors(
        bubbleContainerColorFromMe: Color = RebrandKoinTheme.colors.neutral200.copy(alpha = 0.8f),
        bubbleContainerColorFromOther: Color = RebrandKoinTheme.colors.neutral100,
        bubbleContentColor: Color = RebrandKoinTheme.colors.neutral800,
        bubbleTimeStampColor: Color = RebrandKoinTheme.colors.neutral500,
        userNicknameColor: Color = RebrandKoinTheme.colors.neutral600,
        leftUserLabelColor: Color = RebrandKoinTheme.colors.primary500,
        readCountColor: Color = RebrandKoinTheme.colors.neutral700
    ): GroupChatMessageColors = GroupChatMessageColors(
        bubbleContainerColorFromMe = bubbleContainerColorFromMe,
        bubbleContainerColorFromOther = bubbleContainerColorFromOther,
        bubbleContentColor = bubbleContentColor,
        timeStampColor = bubbleTimeStampColor,
        userNicknameColor = userNicknameColor,
        leftUserLabelColor = leftUserLabelColor,
        readCountColor = readCountColor
    )
}

@Immutable
data class GroupChatMessageColors(
    val bubbleContainerColorFromMe: Color,
    val bubbleContainerColorFromOther: Color,
    val bubbleContentColor: Color,
    val timeStampColor: Color,
    val userNicknameColor: Color,
    val leftUserLabelColor: Color,
    val readCountColor: Color
)

@Composable
fun GroupChatMessageBubble(
    message: GroupChatMessage,
    userColorIndex: Int,
    modifier: Modifier = Modifier,
    colors: GroupChatMessageColors = GroupChatMessageDefaults.purpleColors(),
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    if (message.isSentByMe) {
        GroupChatMessageFromMe(
            message = message,
            colors = colors,
            onShowImageChange = onShowImageChange,
            modifier = modifier
        )
    } else {
        GroupChatMessageFromOther(
            message = message,
            userColorIndex = userColorIndex,
            colors = colors,
            onShowImageChange = onShowImageChange,
            modifier = modifier
        )
    }
}

@Composable
private fun GroupChatMessageFromMe(
    message: GroupChatMessage,
    modifier: Modifier = Modifier,
    colors: GroupChatMessageColors = GroupChatMessageDefaults.purpleColors(),
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            if (message.readCount > 0) {
                Text(
                    text = "${message.readCount}",
                    style = RebrandKoinTheme.typography.regular12,
                    color = colors.readCountColor
                )
            }
            Text(
                text = message.timestamp,
                style = RebrandKoinTheme.typography.regular12,
                color = colors.timeStampColor
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.weight(1f, false)
        ) {
            if (message.isImage) {
                GroupChatMessageImage(
                    imageUrl = message.content,
                    onShowImageChange = onShowImageChange
                )
            } else {
                Text(
                    modifier = Modifier
                        .clip(RebrandKoinTheme.shapes.small)
                        .background(colors.bubbleContainerColorFromMe)
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    text = message.content,
                    style = RebrandKoinTheme.typography.regular12,
                    color = colors.bubbleContentColor
                )
            }
        }
    }
}

@Composable
private fun GroupChatMessageFromOther(
    message: GroupChatMessage,
    userColorIndex: Int,
    modifier: Modifier = Modifier,
    colors: GroupChatMessageColors = GroupChatMessageDefaults.purpleColors(),
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
    ) {
        if (message.isFirstInGroup) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroupChatUserIcon(
                    colorIndex = userColorIndex
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message.userNickname,
                    style = RebrandKoinTheme.typography.regular12,
                    color = colors.userNicknameColor
                )
                if (message.isLeftUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.group_chat_left_user_label),
                        style = RebrandKoinTheme.typography.regular10,
                        color = colors.leftUserLabelColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.weight(1f, false)
            ) {
                if (message.isImage) {
                    GroupChatMessageImage(
                        imageUrl = message.content,
                        onShowImageChange = onShowImageChange
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .clip(RebrandKoinTheme.shapes.small)
                            .background(colors.bubbleContainerColorFromOther)
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        text = message.content,
                        style = RebrandKoinTheme.typography.regular12,
                        color = colors.bubbleContentColor
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message.timestamp,
                style = RebrandKoinTheme.typography.regular12,
                color = colors.timeStampColor
            )
        }
    }
}

@Composable
private fun GroupChatMessageImage(
    imageUrl: String,
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Box {
        SubcomposeAsyncImage(
            modifier = Modifier
                .clip(RebrandKoinTheme.shapes.small)
                .noRippleClickable {
                    onShowImageChange(true, imageUrl.toUri())
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
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
            contentDescription = stringResource(id = R.string.group_chat_message_image)
        )
        if (imageUrl.toUri().scheme == ContentResolver.SCHEME_CONTENT) {
            Box(
                modifier = Modifier
                    .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.4f))
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_uploading_image),
                            contentDescription = stringResource(id = R.string.group_chat_uploading_image)
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.matchParentSize(),
                            strokeWidth = 2.dp,
                            color = RebrandKoinTheme.colors.neutral0
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.chat_loading),
                        color = RebrandKoinTheme.colors.neutral0,
                        style = RebrandKoinTheme.typography.regular10
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupChatMessageBubblePreview() {
    KoinSurface {
        Column {
            GroupChatMessageBubble(
                message = GroupChatPreviewData.otherMessage(
                    id = "1",
                    content = "넵!!",
                    timestamp = "13:53",
                    isFirstInGroup = true
                ),
                userColorIndex = 0
            )
            GroupChatMessageBubble(
                message = GroupChatPreviewData.otherMessage(
                    id = "2",
                    content = "방송국 건너편인가요?",
                    timestamp = "14:53"
                ),
                userColorIndex = 1
            )
        }
    }
}
