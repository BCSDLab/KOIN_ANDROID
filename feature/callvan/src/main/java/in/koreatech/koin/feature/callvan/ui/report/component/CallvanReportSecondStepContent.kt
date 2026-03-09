package `in`.koreatech.koin.feature.callvan.ui.report.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal const val CALLVAN_REPORT_DETAIL_MAX_LENGTH = 1000
internal const val CALLVAN_REPORT_IMAGE_MAX_COUNT = 10

@Composable
internal fun CallvanReportSecondStepContent(
    detail: String,
    onDetailChange: (String) -> Unit,
    images: ImmutableList<Uri>,
    onAddImageClick: () -> Unit,
    onRemoveImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.callvan_report_detail_title),
                    style = KoinTheme.typography.bold18.copy(fontWeight = FontWeight.SemiBold),
                    color = KoinTheme.colors.neutral800
                )
                Text(
                    text = stringResource(
                        R.string.callvan_report_detail_length,
                        detail.length,
                        CALLVAN_REPORT_DETAIL_MAX_LENGTH
                    ),
                    style = KoinTheme.typography.regular12,
                    color = if (detail.length >= CALLVAN_REPORT_DETAIL_MAX_LENGTH) {
                        RebrandKoinTheme.colors.primary500
                    } else {
                        KoinTheme.colors.neutral500
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            CallvanReportTextField(
                modifier = Modifier.height(200.dp),
                value = detail,
                onValueChange = onDetailChange,
                maxLength = CALLVAN_REPORT_DETAIL_MAX_LENGTH,
                placeholder = stringResource(R.string.callvan_report_detail_placeholder),
                contentAlignment = Alignment.TopStart
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = KoinTheme.colors.neutral200
        )

        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.callvan_report_evidence_title),
                style = KoinTheme.typography.bold18.copy(fontWeight = FontWeight.SemiBold),
                color = KoinTheme.colors.neutral800
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.callvan_report_evidence_description),
                style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral600
            )
            Text(
                text = stringResource(
                    R.string.callvan_report_evidence_count,
                    images.size,
                    CALLVAN_REPORT_IMAGE_MAX_COUNT
                ),
                style = KoinTheme.typography.regular12,
                color = if (images.size >= CALLVAN_REPORT_IMAGE_MAX_COUNT) {
                    RebrandKoinTheme.colors.primary500
                } else {
                    KoinTheme.colors.neutral500
                },
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = KoinTheme.colors.neutral100,
                        shape = KoinTheme.shapes.extraSmall
                    )
            ) {
                if (images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(images) { uri ->
                            CallvanReportImageItem(
                                uri = uri,
                                onRemoveClick = { onRemoveImage(uri) },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (images.size < CALLVAN_REPORT_IMAGE_MAX_COUNT) {
                TextButton(
                    onClick = onAddImageClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = RebrandKoinTheme.colors.primary100,
                        contentColor = RebrandKoinTheme.colors.primary900
                    ),
                    shape = KoinTheme.shapes.small
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_picture),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.callvan_report_add_photo),
                        style = KoinTheme.typography.medium16
                    )
                }
            }
        }
    }
}

@Composable
private fun CallvanReportImageItem(
    uri: Uri,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(KoinTheme.shapes.extraSmall)
                .background(KoinTheme.colors.neutral200)
        )
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-8).dp)
                .size(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = RebrandKoinTheme.colors.primary900,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bottom_sheet_close),
                    contentDescription = stringResource(R.string.callvan_report_image_remove),
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportSecondStepContentEmptyPreview() {
    CallvanReportSecondStepContent(
        detail = "",
        onDetailChange = {},
        images = persistentListOf(),
        onAddImageClick = {},
        onRemoveImage = {},
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportSecondStepContentWithImagesPreview() {
    CallvanReportSecondStepContent(
        detail = "",
        onDetailChange = {},
        images = persistentListOf(
            Uri.parse("content://preview/1"),
            Uri.parse("content://preview/2"),
            Uri.parse("content://preview/3")
        ),
        onAddImageClick = {},
        onRemoveImage = {},
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    )
}
