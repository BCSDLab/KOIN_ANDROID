package `in`.koreatech.koin.feature.article.ui.lostandfound.write.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.pxToDp
import `in`.koreatech.koin.feature.article.IMAGE_MAX_COUNT
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType

@Composable
fun WriteArticleUploadImage(
    type: LostOrFoundType,
    uploadedImageCount: Int = 0,
    imageList: List<String> = listOf(),
    modifier: Modifier = Modifier,
    onUploadImage: () -> Unit = {},
    onRemoveImage: (index: Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            style = KoinTheme.typography.medium14,
            text = stringResource(id = R.string.image)
        )
        Row {
            Text(
                modifier = Modifier.weight(1f),
                style = KoinTheme.typography.regular12,
                text = when (type) {
                    LostOrFoundType.LOST -> stringResource(id = R.string.upload_image_of_lost_item)
                    LostOrFoundType.FOUND -> stringResource(id = R.string.upload_image_of_found_item)
                },
                color = Color(0xFF8E8E8E)
            )
            Text(
                style = KoinTheme.typography.regular12,
                text = "$uploadedImageCount/$IMAGE_MAX_COUNT",
                color = KoinTheme.colors.neutral500
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (uploadedImageCount > 0) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(KoinTheme.colors.neutral100)
                    .height(123.dp),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(imageList) { index, imageUrl ->
                    WriteArticleUploadImageThumbnail(
                        index = index,
                        imageUrl = Uri.parse(imageUrl)
                    ) {
                        onRemoveImage(index)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = onUploadImage,
            colors = ButtonDefaults.buttonColors(
                containerColor = KoinTheme.colors.info200
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_upload_image),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    color = KoinTheme.colors.primary600,
                    style = KoinTheme.typography.regular14,
                    text = stringResource(id = R.string.upload_image)
                )
            }
        }
    }
}

@Composable
fun WriteArticleUploadImageThumbnail(
    index: Int,
    imageUrl: Uri,
    modifier: Modifier = Modifier,
    removeImage: (index: Int) -> Unit = {}
) {
    var removeButtonPosition by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier.fillMaxWidth()) {
        if (imageUrl == Uri.EMPTY) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            SubcomposeAsyncImage(
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
                contentDescription = null,
                modifier = modifier.onGloballyPositioned {
                    removeButtonPosition = it.positionInParent() +
                        Offset(
                            it.size.width.toFloat(),
                            0f
                        )
                }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_delete_image),
                contentDescription = null,
                modifier = Modifier
                    .offset(
                        x = removeButtonPosition.x.pxToDp - 8.dp,
                        y = removeButtonPosition.y.pxToDp - 8.dp
                    )
                    .noRippleClickable {
                        removeImage(index)
                    }
            )
        }
    }
}
