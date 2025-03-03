package `in`.koreatech.koin.feature.chat.ui.list.component

import android.webkit.URLUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.chat.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatListItem(
    title: String,
    recentMessage: String,
    imageUrl: String,
    lastMessageAt: LocalTime,
    unReadMessageCount: Int,
    modifier: Modifier = Modifier,
) {
    val lastMessageTimeFormatType = DateTimeFormatter.ofPattern("a hh:mm")
    val convertedLastMessageTime by remember {
        mutableStateOf(
            lastMessageAt.format(
                lastMessageTimeFormatType,
            ),
        )
    }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (URLUtil.isValidUrl(imageUrl) && imageUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                modifier =
                    Modifier
                        .size(48.dp),
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                loading = {
                    Box(
                        modifier =
                            Modifier
                                .border(1.dp, KoinTheme.colors.neutral300, shape = KoinTheme.shapes.medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_chat_list_thumbnail),
                            contentDescription = null,
                        )
                    }
                },
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .border(1.dp, KoinTheme.colors.neutral300, shape = KoinTheme.shapes.medium),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_chat_list_thumbnail),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = KoinTheme.typography.medium14,
                    color = KoinTheme.colors.neutral800,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = convertedLastMessageTime,
                    style = KoinTheme.typography.regular12,
                    color = KoinTheme.colors.neutral500,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = recentMessage,
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                if (unReadMessageCount > 0) {
                    Box(
                        modifier =
                            Modifier
                                .clip(CircleShape)
                                .background(KoinTheme.colors.primary500)
                                .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "$unReadMessageCount",
                            style = KoinTheme.typography.regular12,
                            color = KoinTheme.colors.neutral0,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListItemPreview() {
    ChatListItem(
        title = "Title",
        recentMessage = " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vitae risus condimentum leo facilisis luctus. Vestibulum viverra justo eu leo dictum, sit amet fermentum nisi facilisis. Mauris sagittis dignissim massa, ac varius enim faucibus id. Etiam tempus dolor et diam tempus consectetur. Nulla facilisi. Pellentesque ex nisi, varius eu pellentesque in, scelerisque sed nisl. Morbi tincidunt vestibulum sapien, at mattis erat tempus in. Sed consequat non ligula eget eleifend. Nulla tempor eleifend ligula sed dapibus. Nunc fringilla cursus felis. Curabitur egestas arcu non sodales mollis. ",
        imageUrl = "https://www.example.com/image.jpg",
        lastMessageAt = LocalTime.now(),
        unReadMessageCount = 1,
    )
}
