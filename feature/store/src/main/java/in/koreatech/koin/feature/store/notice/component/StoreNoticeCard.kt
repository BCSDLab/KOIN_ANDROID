package `in`.koreatech.koin.feature.store.notice.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StoreNoticeCard(
    title: String,
    description: String,
    dateRange: String,
    imageUris: ImmutableList<String>,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageUris.isEmpty()) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_bbico),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            } else {
                AsyncImage(
                    model = imageUris.first(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
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
                        .clickable { onExpandClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.store_notice_view_details),
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
            Text(
                text = description,
                style = RebrandKoinTheme.typography.medium12,
                color = RebrandKoinTheme.colors.neutral800
            )
            Text(
                text = dateRange,
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun StoreNoticeCardPreview() {
    StoreNoticeCard(
        title = "알바 모집",
        description = "하루 6-8시간 주 2회 정도 알바할 학생을 찾습니다.\n상담은 직접 와서 만나면 돼요.",
        dateRange = "2025.12.12 - 2029.08.31",
        imageUris = persistentListOf(),
        onExpandClick = {}
    )
}
