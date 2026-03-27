package `in`.koreatech.koin.feature.chat.ui.room.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val chatBubbleDefaultModifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)

object ChatBubbleDefaults {
    @Composable
    fun colors(
        bubbleContainerColorFromMe: Color = KoinTheme.colors.neutral100.copy(alpha = 0.8f),
        bubbleContainerColorFromOther: Color = KoinTheme.colors.info100,
        bubbleContentColor: Color = KoinTheme.colors.neutral800,
        bubbleTimeStampColor: Color = KoinTheme.colors.neutral500,
        userNicknameColor: Color = KoinTheme.colors.neutral600,
        iconContainerColor: Color = KoinTheme.colors.primary500,
        iconBorderColor: Color = Color.Unspecified
    ): ChatBubbleColors = ChatBubbleColors(
        bubbleContainerColorFromMe = bubbleContainerColorFromMe,
        bubbleContainerColorFromOther = bubbleContainerColorFromOther,
        bubbleContentColor = bubbleContentColor,
        timeStampColor = bubbleTimeStampColor,
        userNicknameColor = userNicknameColor,
        iconContainerColor = iconContainerColor,
        iconBorderColor = iconBorderColor
    )

    @Composable
    fun purpleColors(
        bubbleContainerColorFromMe: Color = KoinTheme.colors.neutral200,
        bubbleContainerColorFromOther: Color = KoinTheme.colors.neutral100,
        bubbleContentColor: Color = KoinTheme.colors.neutral800,
        bubbleTimeStampColor: Color = KoinTheme.colors.neutral500,
        userNicknameColor: Color = KoinTheme.colors.neutral600,
        iconContainerColor: Color = Color.Unspecified,
        iconBorderColor: Color = Color.Unspecified
    ): ChatBubbleColors = ChatBubbleColors(
        bubbleContainerColorFromMe = bubbleContainerColorFromMe,
        bubbleContainerColorFromOther = bubbleContainerColorFromOther,
        bubbleContentColor = bubbleContentColor,
        timeStampColor = bubbleTimeStampColor,
        userNicknameColor = userNicknameColor,
        iconContainerColor = iconContainerColor,
        iconBorderColor = iconBorderColor
    )
}

@Immutable
class ChatBubbleColors(
    val bubbleContainerColorFromMe: Color,
    val bubbleContainerColorFromOther: Color,
    val bubbleContentColor: Color,
    val timeStampColor: Color,
    val userNicknameColor: Color,
    val iconContainerColor: Color,
    val iconBorderColor: Color
)

@Composable
fun ChatBubble(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier,
    colors: ChatBubbleColors = ChatBubbleDefaults.colors(),
    chatPartnerProfileImage: Uri? = null,
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    if (message.isSentByMe) {
        ChatBubbleFromMe(
            message = message,
            onShowImageChange = onShowImageChange,
            colors = colors,
            modifier = modifier.then(chatBubbleDefaultModifier)
        )
    } else {
        ChatBubbleFromOther(
            message = message,
            chatPartnerProfileImage = chatPartnerProfileImage,
            onShowImageChange = onShowImageChange,
            colors = colors,
            modifier = modifier.then(chatBubbleDefaultModifier)
        )
    }
}

@Composable
private fun ChatBubbleFromMe(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier,
    colors: ChatBubbleColors = ChatBubbleDefaults.colors(),
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = KoinTheme.typography.regular12,
            color = colors.timeStampColor
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .background(
                    color = colors.bubbleContainerColorFromMe,
                    shape = KoinTheme.shapes.small
                )
                .then(
                    if (message.isImage) {
                        Modifier
                    } else {
                        Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    }
                )
        ) {
            if (message.isImage) {
                ChatBubbleImage(
                    imageUrl = message.content,
                    onShowImageChange = onShowImageChange
                )
            } else {
                ChatBubbleText(
                    textColor = colors.bubbleContentColor,
                    message = message.content
                )
            }
        }
    }
}

@Composable
private fun ChatBubbleFromOther(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier,
    colors: ChatBubbleColors = ChatBubbleDefaults.colors(),
    chatPartnerProfileImage: Uri? = null,
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (chatPartnerProfileImage == null || chatPartnerProfileImage == Uri.EMPTY) {
                Image(
                    painter = painterResource(id = R.drawable.ic_chat_user_image),
                    contentDescription = stringResource(id = R.string.chat_user_profile_image),
                    modifier = Modifier
                        .clip(KoinTheme.shapes.small)
                        .border(width = 1.dp, color = colors.iconBorderColor, shape = KoinTheme.shapes.small)
                        .background(colors.iconContainerColor)
                        .padding(2.dp)
                        .size(24.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chatPartnerProfileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.chat_user_profile_image),
                    placeholder = painterResource(id = R.drawable.ic_chat_user_image)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = message.userNickname,
                style = KoinTheme.typography.regular12,
                color = colors.userNicknameColor
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = colors.bubbleContainerColorFromOther,
                        shape = KoinTheme.shapes.small
                    )
                    .then(
                        if (message.isImage) {
                            Modifier
                        } else {
                            Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                        }
                    )
            ) {
                if (message.isImage) {
                    ChatBubbleImage(
                        imageUrl = message.content,
                        onShowImageChange = onShowImageChange
                    )
                } else {
                    ChatBubbleText(
                        textColor = colors.bubbleContentColor,
                        message = message.content
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = KoinTheme.typography.regular12,
                color = colors.timeStampColor
            )
        }
    }
}

@Composable
private fun ChatBubbleText(
    textColor: Color,
    message: String
) {
    Text(
        text = message,
        style = KoinTheme.typography.regular12,
        color = textColor
    )
}

@Composable
private fun ChatBubbleImage(
    imageUrl: String,
    onShowImageChange: (Boolean, Uri) -> Unit = { _, _ -> }
) {
    Box {
        SubcomposeAsyncImage(
            modifier = Modifier
                .clip(KoinTheme.shapes.small)
                .noRippleClickable {
                    onShowImageChange(true, Uri.parse(imageUrl))
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
            contentDescription = null
        )
        if (imageUrl.startsWith("content://")) {
            Box(
                Modifier
                    .background(Color(0x66000000))
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
                            painter = painterResource(id = R.drawable.ic_uploading_image),
                            contentDescription = null
                        )
                        CircularProgressIndicator(
                            modifier = Modifier.matchParentSize(),
                            strokeWidth = 2.dp,
                            color = KoinTheme.colors.neutral0
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.chat_loading),
                        color = KoinTheme.colors.neutral0,
                        style = KoinTheme.typography.regular10
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatBubblePreview() {
    KoinSurface {
        Column {
            ChatBubble(
                message = ConvertedChatMessage(
                    userId = 0,
                    userNickname = "Me",
                    content = "투명 케이스가 끼워져 있었어요! \n담헌실학관 401호 앞에 떨어져있었어요",
                    timestamp = LocalDateTime.now(),
                    isImage = false,
                    isSentByMe = true,
                    uploadId = "preview_me"
                )
            )

            ChatBubble(
                message = ConvertedChatMessage(
                    userId = 0,
                    userNickname = "Me",
                    content = "투명 케이스가 끼워져 있었어요! \n담헌실학관 401호 앞에 떨어져있었어요",
                    timestamp = LocalDateTime.now(),
                    isImage = false,
                    isSentByMe = false,
                    uploadId = "preview_other"
                )
            )
        }
    }
}
