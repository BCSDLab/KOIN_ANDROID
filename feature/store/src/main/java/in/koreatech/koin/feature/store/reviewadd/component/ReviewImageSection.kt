package `in`.koreatech.koin.feature.store.reviewadd.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.reviewadd.constants.MAX_IMAGE_COUNT
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReviewImageSection(
    imageUris: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onAddImages: () -> Unit = {},
    onRemoveImage: (Int) -> Unit = {}
) {
    val maxItems = MAX_IMAGE_COUNT - imageUris.size

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        BasicText(
            text = stringResource(R.string.photo),
            style = RebrandKoinTheme.typography.medium16
        )

        BasicText(
            text = stringResource(R.string.review_image_upload_description),
            style = RebrandKoinTheme.typography.regular12
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ImageUploadButton(
                    currentCount = imageUris.size,
                    maxCount = MAX_IMAGE_COUNT,
                    onClick = {
                        if (maxItems > 0) {
                            onAddImages()
                        }
                    }
                )
            }
            itemsIndexed(imageUris) { index, uriString ->
                Box(modifier = Modifier.size(97.dp)) {
                    AsyncImage(
                        model = uriString,
                        contentDescription = "",
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RebrandKoinTheme.shapes.small)
                            .align(Alignment.BottomStart),
                        contentScale = ContentScale.Crop
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .offset(x = 84.dp, y = (-3).dp)
                            .size(16.dp)
                            .clickable {
                                onRemoveImage(index)
                            }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewImageSectionPreview() {
    val imageUris = persistentListOf("", "")
    ReviewImageSection(
        imageUris = imageUris
    )
}
