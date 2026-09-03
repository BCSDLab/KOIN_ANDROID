package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentRole
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterButton
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component.CloseRecruitmentDialog
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component.MyRecruitmentEmptyState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component.RecruitmentFilterBottomSheet
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component.RecruitmentPostCard
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val LOAD_MORE_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecruitmentScreen(
    viewModel: MyRecruitmentViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onApplicantManage: (Int) -> Unit = {},
    onChat: (Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            MyRecruitmentSideEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    Scaffold(
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_my_post_title),
                onNavigationIconClick = onNavigateUp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50
                )
            )
        }
    ) { innerPadding ->
        MyRecruitmentScreenImpl(
            posts = state.posts,
            isLoading = state.isLoading,
            isLoadingMore = state.isLoadingMore,
            hasMore = state.currentPage < state.totalPage,
            onLoadMore = viewModel::loadMoreMyRecruitmentPosts,
            onApplicantManage = onApplicantManage,
            onCloseRecruitment = { postId -> viewModel.showCloseDialog(postId) },
            onChat = onChat,
            onFilter = { viewModel.showFilterSheet() },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (state.showCloseDialog) {
        CloseRecruitmentDialog(
            onDismiss = { viewModel.dismissCloseDialog() },
            onConfirm = { viewModel.confirmClose() }
        )
    }

    if (state.showFilterSheet) {
        RecruitmentFilterBottomSheet(
            currentFilter = state.filter,
            onDismiss = { viewModel.dismissFilterSheet() },
            onApply = { viewModel.applyFilter(it) }
        )
    }
}

@Composable
private fun MyRecruitmentScreenImpl(
    posts: ImmutableList<MyRecruitmentPost>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    hasMore: Boolean = false,
    onLoadMore: () -> Unit = {},
    onApplicantManage: (Int) -> Unit = {},
    onCloseRecruitment: (Int) -> Unit = {},
    onChat: (Int) -> Unit = {},
    onFilter: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.5.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.recruitment_my_post_count, posts.size),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            Spacer(modifier = Modifier.weight(1f))
            RecruitmentFilterButton(onClick = onFilter)
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MyRecruitmentEmptyState()
            }
        } else {
            val listState = rememberLazyListState()

            LaunchedEffect(listState, hasMore, isLoadingMore, posts.size) {
                snapshotFlow {
                    val layoutInfo = listState.layoutInfo
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    lastVisibleItemIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
                }
                    .distinctUntilChanged()
                    .filter { it }
                    .collect { if (hasMore && !isLoadingMore) onLoadMore() }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(start = 21.5.dp, end = 21.5.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts, key = { it.id }) { post ->
                    RecruitmentPostCard(
                        post = post,
                        onApplicantManage = { onApplicantManage(post.id) },
                        onCloseRecruitment = if (post.status is RecruitmentStatus.Recruiting) {
                            { onCloseRecruitment(post.id) }
                        } else {
                            null
                        },
                        onChat = { onChat(post.id) }
                    )
                }
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}

private const val PREVIEW_DATE_RANGE = "2026.07.26 ~ 2026.08.07"

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun MyRecruitmentScreenWithPostsPreview() {
    RebrandKoinTheme {
        MyRecruitmentScreenImpl(
            isLoading = false,
            posts = persistentListOf(
                MyRecruitmentPost(
                    id = 1,
                    category = RecruitmentCategory.CONTEST,
                    status = RecruitmentStatus.Recruiting(daysLeft = 5),
                    title = "AI 아이디어 공모전 팀원 모집",
                    roles = persistentListOf(
                        RecruitmentRole("프론트엔드", 1),
                        RecruitmentRole("백엔드", 1),
                        RecruitmentRole("디자인", 1)
                    ),
                    location = "온라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 0,
                    maxApplicants = 3
                ),
                MyRecruitmentPost(
                    id = 2,
                    category = RecruitmentCategory.EXTERNAL_ACTIVITY,
                    status = RecruitmentStatus.Complete,
                    title = "2026 대외활동 팀원 모집",
                    location = "온·오프라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 5,
                    maxApplicants = 5
                ),
                MyRecruitmentPost(
                    id = 3,
                    category = RecruitmentCategory.STUDY,
                    status = RecruitmentStatus.Complete,
                    title = "2026 스터디 팀원 모집",
                    location = "온·오프라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 3,
                    maxApplicants = 3
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun MyRecruitmentScreenEmptyPreview() {
    RebrandKoinTheme {
        MyRecruitmentScreenImpl(isLoading = false, posts = persistentListOf())
    }
}
