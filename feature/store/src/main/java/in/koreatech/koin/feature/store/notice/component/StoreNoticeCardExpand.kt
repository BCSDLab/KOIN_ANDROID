package `in`.koreatech.koin.feature.store.notice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

@Composable
fun StoreNoticeCardExpand(
    title: String,
    description: String,
    dateRange: String,
    imageUris: List<String>,
    onFoldClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = RebrandKoinTheme.typography.bold15,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onFoldClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.store_notice_fold),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_up),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
            Text(
                text = dateRange,
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
        val pagerState = rememberPagerState(pageCount = { imageUris.size.coerceAtLeast(1) })
        if (imageUris.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .background(RebrandKoinTheme.colors.neutral100),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_bbico),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(
                        text = stringResource(R.string.store_notice_image_preparing),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                AsyncImage(
                    model = imageUris[page],
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        if (imageUris.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(imageUris.size) { index ->
                    val selected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) {
                                    RebrandKoinTheme.colors.neutral800
                                } else {
                                    RebrandKoinTheme.colors.neutral300
                                }
                            )
                    )
                }
            }
        }
        Text(
            text = description,
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral800
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreNoticeCardExpandPreview() {
    StoreNoticeCardExpand(
        title = "알바 모집",
        description = "하루 6-8시간 주 2회 정도 알바할 학생을 찾습니다.\n상담은 직접 와서 만나면 돼요.",
        dateRange = "2025.12.12 - 2029.08.31",
        imageUris = emptyList(),
        onFoldClick = {}
    )
}
