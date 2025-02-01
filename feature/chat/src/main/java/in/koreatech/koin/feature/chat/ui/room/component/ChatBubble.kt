package `in`.koreatech.koin.feature.chat.ui.room.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.R
import `in`.koreatech.koin.feature.chat.ui.model.ConvertedChatMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val chatBubbleDefaultModifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)

@Composable
fun ChatBubble(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier,
    chatPartnerProfileImage: Uri? = null
) {
    if (message.isSentByMe) {
        ChatBubbleFromMe(
            message = message,
            modifier = modifier.then(chatBubbleDefaultModifier)
        )
    } else {
        ChatBubbleFromOther(
            message = message,
            chatPartnerProfileImage = chatPartnerProfileImage,
            modifier = modifier.then(chatBubbleDefaultModifier)
        )
    }
}

@Composable
private fun ChatBubbleFromMe(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral500,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .background(
                    color = KoinTheme.colors.neutral100.copy(alpha = 0.8f),
                    shape = KoinTheme.shapes.small,
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            if (message.isImage) {
                ChatBubbleImage(message.content)
            } else {
                ChatBubbleText(message.content)
            }
        }
    }
}

@Composable
private fun ChatBubbleFromOther(
    message: ConvertedChatMessage,
    modifier: Modifier = Modifier,
    chatPartnerProfileImage: Uri? = null
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
                    modifier = Modifier.size(24.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chatPartnerProfileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.chat_user_profile_image),
                    placeholder = painterResource(id = R.drawable.ic_chat_user_image),
                )
            }
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = message.userNickname,
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral600,
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
                        color = KoinTheme.colors.info100,
                        shape = KoinTheme.shapes.small
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                if (message.isImage) {
                    ChatBubbleImage(message.content)
                } else {
                    ChatBubbleText(message.content)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500,
            )
        }
    }
}

@Composable
private fun ChatBubbleText(
    message: String
) {
    Text(
        text = message,
        style = KoinTheme.typography.regular12,
        color = KoinTheme.colors.neutral800
    )
}

@Composable
private fun ChatBubbleImage(
    imageUrl: String
) {
    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxWidth(),
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
}

@Preview
@Composable
fun ChatBubblePreview() {
    KoinSurface {
        ChatBubble(
            message = ConvertedChatMessage(
                userId = 0,
                userNickname = "Me",
                content = "투명 케이스가 끼워져 있었어요! \n담헌실학관 401호 앞에 떨어져있었어요",
                timestamp = LocalDateTime.now(),
                isImage = false,
                isSentByMe = true
            )
        )
    }
}
