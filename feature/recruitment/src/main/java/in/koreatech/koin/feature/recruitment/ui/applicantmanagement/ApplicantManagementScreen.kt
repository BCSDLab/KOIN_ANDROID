package `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.component.ApplicantListItem
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.component.ApplicantManagementEmptyState
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.component.ApplicantManagementPostCard
import `in`.koreatech.koin.feature.recruitment.ui.applicantmanagement.model.Applicant
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantManagementScreen(
    viewModel: ApplicantManagementViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onChat: () -> Unit = {},
    onApplicantDetail: (Long) -> Unit = {},
    onMoreOptions: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    Scaffold(
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_applicant_management_title),
                onNavigationIconClick = onNavigateUp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50
                ),
                actions = {
                    IconButton(onClick = onMoreOptions) {
                        Icon(
                            painter = painterResource(R.drawable.ic_recruitment_uim_process),
                            contentDescription = null,
                            tint = RebrandKoinTheme.colors.neutral700
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ApplicantManagementScreenImpl(
            post = state.post,
            applicants = state.applicants,
            onChat = onChat,
            onApplicantDetail = onApplicantDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ApplicantManagementScreenImpl(
    post: MyRecruitmentPost?,
    applicants: ImmutableList<Applicant>,
    modifier: Modifier = Modifier,
    onChat: () -> Unit = {},
    onApplicantDetail: (Long) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 21.5.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (post != null) {
            item {
                ApplicantManagementPostCard(post = post, onChat = onChat)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.recruitment_applicant_list_title),
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Text(
                    text = stringResource(R.string.recruitment_applicant_list_count, applicants.size),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }
        if (applicants.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ApplicantManagementEmptyState()
                }
            }
        } else {
            items(applicants, key = { it.id }) { applicant ->
                ApplicantListItem(
                    applicant = applicant,
                    onClick = { onApplicantDetail(applicant.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantManagementScreenWithApplicantsPreview() {
    RebrandKoinTheme {
        ApplicantManagementScreenImpl(
            post = MyRecruitmentPost(
                id = 1L,
                category = RecruitmentCategory.CONTEST,
                status = RecruitmentStatus.Recruiting(daysLeft = 5),
                title = "AI 아이디어 공모전 팀원 모집",
                location = "온라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 2,
                maxApplicants = 3
            ),
            applicants = persistentListOf(
                Applicant(1L, "김철수", "백엔드", "컴퓨터공학부", "23학번", ApplicantStatus.REJECTED),
                Applicant(2L, "김철수", "디자인", "컴퓨터공학부", "23학번", ApplicantStatus.APPROVED, hasChatRoom = true),
                Applicant(3L, "김철수", "프론트엔드", "컴퓨터공학부", "23학번", ApplicantStatus.PENDING)
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantManagementScreenEmptyPreview() {
    RebrandKoinTheme {
        ApplicantManagementScreenImpl(
            post = MyRecruitmentPost(
                id = 1L,
                category = RecruitmentCategory.CONTEST,
                status = RecruitmentStatus.Recruiting(daysLeft = 5),
                title = "AI 아이디어 공모전 팀원 모집",
                location = "온라인",
                dateRange = "2026.07.26 ~ 2026.08.07",
                currentApplicants = 0,
                maxApplicants = 3
            ),
            applicants = persistentListOf()
        )
    }
}
