package `in`.koreatech.koin.feature.chat.ui.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.chat.R

private const val IMAGE_MAX_WIDTH_FRACTION = 0.6f
private val ImageMinHeight = 100.dp

@Composable
fun ChatMessageBubble(
    content: String,
    timestamp: String,
    isSentByMe: Boolean,
    modifier: Modifier = Modifier,
    isImage: Boolean = false,
    authorNickname: String? = null,
    avatar: (@Composable () -> Unit)? = null,
    isLeftUser: Boolean = false,
    isUploading: Boolean = false,
    onImageClick: (Uri) -> Unit = {}
) {
    Column(modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp)) {
        if (!isSentByMe && !authorNickname.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (avatar != null) {
                    avatar()
                    Spacer(Modifier.width(4.dp))
                }
                Text(
                    text = authorNickname,
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral600
                )
                if (isLeftUser) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.chat_left_user_label),
                        style = RebrandKoinTheme.typography.regular10,
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
        ) {
            if (isSentByMe) {
                ChatMessageTime(timestamp)
                Spacer(Modifier.width(8.dp))
            }
            Box(Modifier.weight(1f, false)) {
                if (isImage) {
                    ChatImage(
                        imageUrl = content,
                        modifier = Modifier
                            .fillMaxWidth(IMAGE_MAX_WIDTH_FRACTION)
                            .heightIn(min = ImageMinHeight)
                            .clip(RebrandKoinTheme.shapes.small),
                        isUploading = isUploading,
                        onImageClick = onImageClick
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .clip(RebrandKoinTheme.shapes.small)
                            .background(
                                if (isSentByMe) RebrandKoinTheme.colors.neutral200.copy(alpha = 0.8f) else RebrandKoinTheme.colors.neutral100
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        text = content,
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral800
                    )
                }
            }
            if (!isSentByMe) {
                Spacer(Modifier.width(8.dp))
                ChatMessageTime(timestamp)
            }
        }
    }
}

@Composable
private fun ChatMessageTime(timestamp: String) {
    Text(
        text = timestamp,
        style = RebrandKoinTheme.typography.regular12,
        color = RebrandKoinTheme.colors.neutral500
    )
}

@Composable
internal fun ChatImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    isUploading: Boolean = false,
    onImageClick: ((Uri) -> Unit)? = null
) {
    val context = LocalContext.current
    val imageUri = remember(imageUrl) { imageUrl.toUri() }
    val request = remember(context, imageUrl) {
        ImageRequest.Builder(context).data(imageUrl).crossfade(true).build()
    }
    Box(
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            modifier = modifier.then(
                if (onImageClick != null && !isUploading) Modifier.noRippleClickable { onImageClick(imageUri) } else Modifier
            ),
            model = request,
            loading = { ChatProgressIndicator(Modifier.heightIn(min = ImageMinHeight)) },
            error = {
                Box(
                    modifier = Modifier.fillMaxWidth().heightIn(min = ImageMinHeight).background(RebrandKoinTheme.colors.neutral100),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.chat_image_load_failed),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            },
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(if (isUploading) R.string.chat_uploading_image else R.string.chat_message_image)
        )
        if (isUploading) {
            Box(
                modifier = Modifier.matchParentSize().clip(RebrandKoinTheme.shapes.small).background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RebrandKoinTheme.colors.primary500)
            }
        }
    }
}
