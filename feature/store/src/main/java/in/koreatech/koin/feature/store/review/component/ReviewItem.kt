package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.component.dialog.StoreImageDialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReviewItem(
    isMyReview: Boolean,
    userName: String,
    rating: Int,
    date: String,
    content: String,
    modifier: Modifier = Modifier,
    isReported: Boolean = false,
    imageUrls: ImmutableList<String>,
    menuTags: ImmutableList<String>,
    onReportClick: () -> Unit = { },
    onEditClick: () -> Unit = { },
    onDeleteClick: () -> Unit = { }
) {
    if (isReported) {
        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_store_review_reported),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.review_reported),
                color = RebrandKoinTheme.colors.neutral500,
                style = RebrandKoinTheme.typography.regular14
            )
        }
        return
    }
    var dialogOpen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
        textColor = RebrandKoinTheme.colors.neutral500,
        textStyle = RebrandKoinTheme.typography.bold12,
        containerColor = RebrandKoinTheme.colors.neutral200,
        elevation = 0.dp,
        borderWidth = 1.dp,
        shape = CircleShape,
        paddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        ambientColor = Color.Transparent,
        spotColor = Color.Transparent,
        innerPadding = 0.dp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            if (isMyReview) {
                Row(
                    modifier = Modifier.padding(top = 3.dp, bottom = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_beenhere),
                        contentDescription = "",
                        tint = RebrandKoinTheme.colors.primary500
                    )
                    BasicText(
                        text = stringResource(R.string.review_mine),
                        style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.primary500)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicText(
                    text = userName,
                    style = RebrandKoinTheme.typography.medium16.copy(color = RebrandKoinTheme.colors.neutral800)
                )
                if (isMyReview) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        KoinStoreChip(
                            text = stringResource(R.string.edit),
                            chipStyle = chipStyle,
                            onClick = { onEditClick() }
                        )
                        KoinStoreChip(
                            text = stringResource(R.string.delete),
                            chipStyle = chipStyle,
                            onClick = { onDeleteClick() }
                        )
                    }
                } else {
                    BasicText(
                        text = stringResource(R.string.review_report),
                        style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral500),
                        modifier = Modifier.clickable { onReportClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(
                    rating = rating,
                    iconSize = 16.dp
                )
                BasicText(
                    text = date,
                    style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.neutral500)
                )
            }

            if (content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))

                BasicText(
                    text = content,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (imageUrls.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(imageUrls) { index, imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(148.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                selectedIndex = index
                                dialogOpen = true
                            }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (menuTags.isNotEmpty()) {
            MenuTagChipGroup(
                menuTags = menuTags,
                modifier = Modifier.padding(horizontal = 24.dp),
                isIconVisibility = false,
                enabled = false,
                onClick = { }
            )
        }
    }
    if (dialogOpen) {
        StoreImageDialog(
            imageUrls = imageUrls,
            initialPage = selectedIndex,
            onDismiss = { dialogOpen = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewItemPreview() {
    val tags = persistentListOf("계란찜", "1인 매운 닭발")
    ReviewItem(
        isMyReview = true,
        userName = "익명_123456",
        rating = 4,
        date = "2025.01.01",
        content = "내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요.",
        modifier = Modifier,
        imageUrls = persistentListOf(),
        menuTags = tags,
        onReportClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ReviewItemGuestPreview() {
    val tags = persistentListOf("계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발")
    val imageUrls = persistentListOf("")
    ReviewItem(
        isReported = true,
        isMyReview = false,
        userName = "익명_123456",
        rating = 2,
        date = "2025.01.01",
        content = "내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요.",
        modifier = Modifier,
        imageUrls = imageUrls,
        menuTags = tags,
        onReportClick = {}
    )
}
