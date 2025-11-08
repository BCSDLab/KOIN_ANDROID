package `in`.koreatech.koin.feature.store.reviewadd.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun ReviewImageSection(
    imageUris: List<String>,
    modifier: Modifier = Modifier,
    onAddImages: (List<String>) -> Unit = {},
    onRemoveImage: (Int) -> Unit = {}
) {
    val pickMultipleMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 3)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val selectableCount = 3 - imageUris.size
            val limitedUris = if (uris.size > selectableCount) uris.take(selectableCount) else uris
            onAddImages(limitedUris.map { it.toString() })
        }
    }
    val maxItems = 3 - imageUris.size

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        BasicText(
            text = "사진",
            style = RebrandKoinTheme.typography.medium16
        )

        BasicText(
            text = "리뷰와 관련된 사진을 업로드해주세요.",
            style = RebrandKoinTheme.typography.regular12
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImageUploadButton(
                currentCount = imageUris.size,
                maxCount = 3,
                onClick = {
                    if (maxItems > 0) {
                        pickMultipleMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
            )

            imageUris.forEachIndexed { index, uriString ->
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
    val imageUris = listOf("", "")
    ReviewImageSection(
        imageUris = imageUris
    )
}
