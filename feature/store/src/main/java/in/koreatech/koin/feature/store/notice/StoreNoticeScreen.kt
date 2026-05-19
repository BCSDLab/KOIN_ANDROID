package `in`.koreatech.koin.feature.store.notice

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeCard
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeCardExpand
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeEmpty
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StoreNoticeScreen(
    state: StoreNoticeListState,
    onExpandClick: (Int) -> Unit,
    onFoldClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (state.notices.isEmpty()) {
            StoreNoticeEmpty(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.notices,
                    key = { it.id }
                ) { notice ->
                    AnimatedContent(
                        targetState = notice.isExpanded,
                        label = "NoticeCardAnimation_${notice.id}"
                    ) { isExpanded ->
                        if (isExpanded) {
                            StoreNoticeCardExpand(
                                title = notice.title,
                                description = notice.description,
                                dateRange = notice.dateRange,
                                imageUris = notice.imageUris,
                                onFoldClick = { onFoldClick(notice.id) }
                            )
                        } else {
                            StoreNoticeCard(
                                title = notice.title,
                                description = notice.description,
                                dateRange = notice.dateRange,
                                imageUris = notice.imageUris,
                                onExpandClick = { onExpandClick(notice.id) }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = RebrandKoinTheme.colors.neutral100
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun StoreNoticeScreenPreview() {
    StoreNoticeScreen(
        state = StoreNoticeListState(
            notices = persistentListOf(
                StoreNoticeItemState(
                    id = 1,
                    title = "알바 모집",
                    description = "하루 6-8시간 주 2회 정도 알바할 학생을 찾습니다.\n상담은 직접 와서 만나면 돼요.",
                    dateRange = "2025.12.12 - 2029.08.31",
                    isExpanded = true
                ),
                StoreNoticeItemState(
                    id = 2,
                    title = "알바 모집",
                    description = "하루 6-8시간 주 2회 정도 알바할 학생을 찾습니다.\n상담은 직접 와서 만나면 돼요.",
                    dateRange = "2025.12.12 - 2029.08.31"
                )
            )
        ),
        onExpandClick = {},
        onFoldClick = {}
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun StoreNoticeScreenEmptyPreview() {
    StoreNoticeScreen(
        state = StoreNoticeListState(notices = persistentListOf()),
        onExpandClick = {},
        onFoldClick = {}
    )
}
