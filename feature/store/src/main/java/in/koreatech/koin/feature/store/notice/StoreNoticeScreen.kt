package `in`.koreatech.koin.feature.store.notice

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeCard
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeCardExpand
import `in`.koreatech.koin.feature.store.notice.component.StoreNoticeEmpty
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreNoticeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreNoticeViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            KoinStoreTopAppBar(
                title = state.storeName,
                onNavigationIconClick = {
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_BENEFIT_BACK,
                        state.storeName
                    )
                    onBackClick()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                )
            )
        }
    ) { innerPadding ->
        StoreNoticeContent(
            state = state,
            onExpandClick = viewModel::expandNotice,
            onFoldClick = viewModel::foldNotice,
            modifier = Modifier
                .padding(innerPadding)
                .background(colorResource(id = R.color.store_detail_background))
        )
    }
}

@Composable
private fun StoreNoticeContent(
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
                                onExpandClick = {
                                    EventLogger.logClickEvent(
                                        EventAction.BUSINESS,
                                        AnalyticsConstant.Label.SHOP_BENEFIT_DETAIL,
                                        state.storeName
                                    )
                                    onExpandClick(notice.id)
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        color = RebrandKoinTheme.colors.neutral400
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun StoreNoticeScreenPreview() {
    StoreNoticeContent(
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
    StoreNoticeContent(
        state = StoreNoticeListState(notices = persistentListOf()),
        onExpandClick = {},
        onFoldClick = {}
    )
}
