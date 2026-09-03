package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentEmptyState
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterButton
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component.AppliedRecruitmentFilterBottomSheet
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component.AppliedRecruitmentPostCard
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppliedRecruitmentScreen(
    viewModel: MyAppliedRecruitmentViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            MyAppliedRecruitmentSideEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    Scaffold(
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_applied_title),
                onNavigationIconClick = onNavigateUp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50
                )
            )
        }
    ) { innerPadding ->
        MyAppliedRecruitmentScreenImpl(
            posts = state.posts,
            onFilter = { viewModel.showFilterSheet() },
            modifier = Modifier.padding(innerPadding)
        )
    }

    if (state.showFilterSheet) {
        AppliedRecruitmentFilterBottomSheet(
            currentFilter = state.filter,
            onDismiss = { viewModel.dismissFilterSheet() },
            onApply = { viewModel.applyFilter(it) }
        )
    }
}

@Composable
private fun MyAppliedRecruitmentScreenImpl(
    posts: ImmutableList<AppliedRecruitmentPost>,
    modifier: Modifier = Modifier,
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
                text = stringResource(R.string.recruitment_applied_post_count, posts.size),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            Spacer(modifier = Modifier.weight(1f))
            RecruitmentFilterButton(onClick = onFilter)
        }

        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                RecruitmentEmptyState(
                    title = stringResource(R.string.recruitment_applied_empty_title),
                    subtitle = stringResource(R.string.recruitment_applied_empty_subtitle)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 21.5.dp, end = 21.5.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts, key = { it.id }) { post ->
                    AppliedRecruitmentPostCard(post = post)
                }
            }
        }
    }
}

private const val PREVIEW_DATE_RANGE = "2026.07.26 ~ 2026.08.07"

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun MyAppliedRecruitmentScreenWithPostsPreview() {
    RebrandKoinTheme {
        MyAppliedRecruitmentScreenImpl(
            posts = persistentListOf(
                AppliedRecruitmentPost(
                    id = 1,
                    category = RecruitmentCategory.CONTEST,
                    applicationStatus = AppliedRecruitmentStatus.Approved,
                    daysLeft = 5,
                    title = "AI 아이디어 공모전 팀원 모집",
                    roles = persistentListOf(
                        RecruitmentRole("프론트엔드", 1),
                        RecruitmentRole("백엔드", 1)
                    ),
                    location = "온라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 2,
                    maxApplicants = 3
                ),
                AppliedRecruitmentPost(
                    id = 2,
                    category = RecruitmentCategory.STUDY,
                    applicationStatus = AppliedRecruitmentStatus.Pending,
                    daysLeft = 3,
                    title = "2026 스터디 팀원 모집",
                    location = "온·오프라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 1,
                    maxApplicants = 5
                ),
                AppliedRecruitmentPost(
                    id = 3,
                    category = RecruitmentCategory.EXTERNAL_ACTIVITY,
                    applicationStatus = AppliedRecruitmentStatus.Rejected,
                    daysLeft = null,
                    title = "2026 대외활동 팀원 모집",
                    location = "온·오프라인",
                    dateRange = PREVIEW_DATE_RANGE,
                    currentApplicants = 5,
                    maxApplicants = 5
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun MyAppliedRecruitmentScreenEmptyPreview() {
    RebrandKoinTheme {
        MyAppliedRecruitmentScreenImpl(posts = persistentListOf())
    }
}
