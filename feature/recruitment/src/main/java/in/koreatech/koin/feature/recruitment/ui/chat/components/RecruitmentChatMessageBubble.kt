package `in`.koreatech.koin.feature.recruitment.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

private val ImageMaxWidthFraction = 0.6f
private val ImageMinHeight = 100.dp

object RecruitmentChatMessageDefaults {
    @Composable
    fun colors(
        bubbleContainerColorFromMe: Color = RebrandKoinTheme.colors.neutral200.copy(alpha = 0.8f),
        bubbleContainerColorFromOther: Color = RebrandKoinTheme.colors.neutral100,
        bubbleContentColor: Color = RebrandKoinTheme.colors.neutral800,
        timeStampColor: Color = RebrandKoinTheme.colors.neutral500,
        authorNicknameColor: Color = RebrandKoinTheme.colors.neutral600
    ): RecruitmentChatMessageColors = RecruitmentChatMessageColors(
        bubbleContainerColorFromMe = bubbleContainerColorFromMe,
        bubbleContainerColorFromOther = bubbleContainerColorFromOther,
        bubbleContentColor = bubbleContentColor,
        timeStampColor = timeStampColor,
        authorNicknameColor = authorNicknameColor
    )
}

@Immutable
data class RecruitmentChatMessageColors(
    val bubbleContainerColorFromMe: Color,
    val bubbleContainerColorFromOther: Color,
    val bubbleContentColor: Color,
    val timeStampColor: Color,
    val authorNicknameColor: Color
)

@Composable
fun RecruitmentChatMessageBubble(
    content: String,
    timestamp: String,
    isSentByMe: Boolean,
    modifier: Modifier = Modifier,
    isImage: Boolean = false,
    authorNickname: String? = null,
    avatar: (@Composable () -> Unit)? = null,
    colors: RecruitmentChatMessageColors = RecruitmentChatMessageDefaults.colors()
) {
    if (isSentByMe) {
        RecruitmentChatMessageFromMe(
            content = content,
            timestamp = timestamp,
            isImage = isImage,
            colors = colors,
            modifier = modifier
        )
    } else {
        RecruitmentChatMessageFromOther(
            content = content,
            timestamp = timestamp,
            isImage = isImage,
            authorNickname = authorNickname,
            avatar = avatar,
            colors = colors,
            modifier = modifier
        )
    }
}

@Composable
private fun RecruitmentChatMessageFromMe(
    content: String,
    timestamp: String,
    isImage: Boolean,
    modifier: Modifier = Modifier,
    colors: RecruitmentChatMessageColors = RecruitmentChatMessageDefaults.colors()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = timestamp,
            style = RebrandKoinTheme.typography.regular12,
            color = colors.timeStampColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f, false)) {
            if (isImage) {
                RecruitmentChatMessageImage(imageUrl = content)
            } else {
                Text(
                    modifier = Modifier
                        .clip(RebrandKoinTheme.shapes.small)
                        .background(colors.bubbleContainerColorFromMe)
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    text = content,
                    style = RebrandKoinTheme.typography.regular12,
                    color = colors.bubbleContentColor
                )
            }
        }
    }
}

@Composable
private fun RecruitmentChatMessageFromOther(
    content: String,
    timestamp: String,
    isImage: Boolean,
    authorNickname: String?,
    avatar: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    colors: RecruitmentChatMessageColors = RecruitmentChatMessageDefaults.colors()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
    ) {
        if (!authorNickname.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (avatar != null) {
                    avatar()
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = authorNickname,
                    style = RebrandKoinTheme.typography.regular12,
                    color = colors.authorNicknameColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.weight(1f, false)) {
                if (isImage) {
                    RecruitmentChatMessageImage(imageUrl = content)
                } else {
                    Text(
                        modifier = Modifier
                            .clip(RebrandKoinTheme.shapes.small)
                            .background(colors.bubbleContainerColorFromOther)
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        text = content,
                        style = RebrandKoinTheme.typography.regular12,
                        color = colors.bubbleContentColor
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timestamp,
                style = RebrandKoinTheme.typography.regular12,
                color = colors.timeStampColor
            )
        }
    }
}

@Composable
private fun RecruitmentChatMessageImage(imageUrl: String) {
    SubcomposeAsyncImage(
        modifier = Modifier
            .fillMaxWidth(ImageMaxWidthFraction)
            .heightIn(min = ImageMinHeight)
            .clip(RebrandKoinTheme.shapes.small),
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
        contentDescription = stringResource(id = R.string.recruitment_chat_message_image)
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatMessageBubbleGroupPreview() {
    KoinSurface {
        Column {
            RecruitmentChatMessageBubble(
                content = "안녕하세요! 잘 부탁드립니다.",
                timestamp = "13:53",
                isSentByMe = false,
                authorNickname = "낭만고양이"
            )
            RecruitmentChatMessageBubble(
                content = "네 반갑습니다!",
                timestamp = "13:54",
                isSentByMe = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatMessageBubbleDirectPreview() {
    KoinSurface {
        Column {
            RecruitmentChatMessageBubble(
                content = "안녕하세요! 지원서 잘 봤습니다.",
                timestamp = "13:53",
                isSentByMe = false,
                authorNickname = "낭만고양이",
                avatar = { RecruitmentChatUserIcon() }
            )
            RecruitmentChatMessageBubble(
                content = "감사합니다! 잘 부탁드려요.",
                timestamp = "13:54",
                isSentByMe = true
            )
        }
    }
}
