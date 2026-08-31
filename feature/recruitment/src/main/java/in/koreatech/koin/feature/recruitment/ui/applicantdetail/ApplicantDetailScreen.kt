package `in`.koreatech.koin.feature.recruitment.ui.applicantdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.ApplicantStatus
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component.ApplicantActivityInfoBox
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component.ApplicantDecisionButtons
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component.ApplicantDecisionDialog
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component.ApplicantDetailProfileCard
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.component.ApplicantLabeledInfoBox
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.ApplicantActivity
import `in`.koreatech.koin.feature.recruitment.ui.applicantdetail.model.ApplicantDetail
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantDetailScreen(
    viewModel: ApplicantDetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val applicant = state.applicant

    Scaffold(
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_applicant_detail_title),
                onNavigationIconClick = onNavigateUp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50
                )
            )
        },
        bottomBar = {
            if (applicant != null) {
                ApplicantDecisionButtons(
                    onReject = { viewModel.showRejectDialog() },
                    onApprove = { viewModel.showApproveDialog() },
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 21.5.dp, vertical = 16.dp)
                )
            }
        }
    ) { innerPadding ->
        if (applicant != null) {
            ApplicantDetailScreenImpl(
                applicant = applicant,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (state.showApproveDialog) {
        ApplicantDecisionDialog(
            title = stringResource(R.string.recruitment_applicant_approve_dialog_title),
            message = stringResource(R.string.recruitment_applicant_approve_dialog_message),
            confirmText = stringResource(R.string.recruitment_applicant_approve_dialog_confirm),
            onDismiss = { viewModel.dismissApproveDialog() },
            onConfirm = { viewModel.approve() }
        )
    }

    if (state.showRejectDialog) {
        ApplicantDecisionDialog(
            title = stringResource(R.string.recruitment_applicant_reject_dialog_title),
            message = stringResource(R.string.recruitment_applicant_reject_dialog_message),
            confirmText = stringResource(R.string.recruitment_applicant_reject_dialog_confirm),
            onDismiss = { viewModel.dismissRejectDialog() },
            onConfirm = { viewModel.reject() }
        )
    }
}

@Composable
private fun ApplicantDetailScreenImpl(
    applicant: ApplicantDetail,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 21.5.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ApplicantDetailProfileCard(
                name = applicant.name,
                role = applicant.role,
                department = applicant.department,
                studentNumber = applicant.studentNumber,
                status = applicant.status
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.recruitment_applicant_detail_basic_info),
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ApplicantLabeledInfoBox(
                        label = stringResource(R.string.recruitment_applicant_detail_skills),
                        content = applicant.skills.joinToString()
                    )
                    applicant.activities.forEach { activity ->
                        ApplicantActivityInfoBox(
                            title = activity.title,
                            period = activity.period,
                            content = activity.content
                        )
                    }
                    ApplicantLabeledInfoBox(
                        label = stringResource(R.string.recruitment_applicant_detail_introduction),
                        content = applicant.introduction
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.recruitment_applicant_detail_application_info),
                    style = RebrandKoinTheme.typography.bold16,
                    color = RebrandKoinTheme.colors.neutral800
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ApplicantLabeledInfoBox(
                        label = stringResource(R.string.recruitment_applicant_detail_motivation),
                        content = applicant.motivation
                    )
                    ApplicantLabeledInfoBox(
                        label = stringResource(R.string.recruitment_applicant_detail_available_time),
                        content = applicant.availableTime
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8FA)
@Composable
private fun ApplicantDetailScreenPreview() {
    RebrandKoinTheme {
        ApplicantDetailScreenImpl(
            applicant = ApplicantDetail(
                id = 1L,
                name = "김철수",
                role = "프론트엔드",
                department = "컴퓨터공학부",
                studentNumber = "23학번",
                status = ApplicantStatus.PENDING,
                skills = persistentListOf("정보처리기사"),
                activities = persistentListOf(
                    ApplicantActivity(
                        title = "AI 공모전",
                        period = "2026.03.23 - 2026.04.06",
                        content = "AI 공모전에서 기획을 담당했고 @@@를 주제로 @@@를 만들었습니다"
                    )
                ),
                introduction = "안녕하세요.",
                motivation = "안녕하세요.",
                availableTime = "월 수 금 20시 이후"
            )
        )
    }
}
