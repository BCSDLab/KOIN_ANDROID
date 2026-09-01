package `in`.koreatech.koin.feature.recruitment.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentDeleteDialog
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentInfoSection
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentMoreMenu
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentRoleSection
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentTextSection
import `in`.koreatech.koin.feature.recruitment.ui.detail.component.RecruitmentTitleSection
import `in`.koreatech.koin.feature.recruitment.ui.detail.model.RecruitmentRoleModel
import `in`.koreatech.koin.feature.recruitment.ui.detail.model.RecruitmentType
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RecruitmentDetailScreen(
    viewModel: RecruitmentDetailViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    val state by viewModel.collectAsState()

    RecruitmentDetailScreenImpl(
        state = state,
        onTopbarBackClick = onTopbarBackClick,
        onMoreClick = { viewModel.updateMoreMenuVisible(true) },
        onMoreMenuDismiss = { viewModel.updateMoreMenuVisible(false) },
        onEditClick = { viewModel.updateMoreMenuVisible(false) },
        onDeleteClick = { viewModel.updateDeleteDialogVisible(true) },
        onDeleteDialogDismiss = { viewModel.updateDeleteDialogVisible(false) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecruitmentDetailScreenImpl(
    state: RecruitmentDetailState,
    modifier: Modifier = Modifier,
    onTopbarBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onMoreMenuDismiss: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDeleteDialogDismiss: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_top_bar_title),
                textStyle = RebrandKoinTheme.typography.bold16,
                onNavigationIconClick = onTopbarBackClick,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50,
                    navigationIconContentColor = RebrandKoinTheme.colors.neutral800,
                    titleContentColor = RebrandKoinTheme.colors.neutral800,
                    actionIconContentColor = RebrandKoinTheme.colors.neutral800
                ),
                actions = {
                    if (state.isAuthor) {
                        Box(modifier = Modifier.padding(end = 12.dp)) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .noRippleClickable(onClick = onMoreClick),
                                imageVector = ImageVector.vectorResource(
                                    R.drawable.ic_recruitment_more
                                ),
                                contentDescription = stringResource(
                                    R.string.recruitment_more_content_description
                                ),
                                tint = RebrandKoinTheme.colors.neutral800
                            )
                            RecruitmentMoreMenu(
                                expanded = state.isMoreMenuVisible,
                                onDismissRequest = onMoreMenuDismiss,
                                onEditClick = onEditClick,
                                onDeleteClick = onDeleteClick
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            RecruitmentDetailBottomAction(
                isAuthor = state.isAuthor,
                isClosed = state.isClosed
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            RecruitmentTitleSection(
                modifier = Modifier.padding(horizontal = 36.dp),
                category = state.category,
                dDay = state.dDay,
                isClosed = state.isClosed,
                title = state.title
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 1.dp,
                color = RebrandKoinTheme.colors.neutral300
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                RecruitmentInfoSection(
                    location = state.location,
                    activityStartDate = state.activityStartDate,
                    activityEndDate = state.activityEndDate,
                    currentParticipants = state.currentParticipants,
                    maxParticipants = state.maxParticipants,
                    createdAt = state.createdAt,
                    authorNickname = state.authorNickname
                )
                if (state.recruitmentType == RecruitmentType.ROLE_BASED) {
                    RecruitmentRoleSection(roles = state.roles)
                }
                RecruitmentTextSection(
                    title = stringResource(R.string.recruitment_section_description),
                    content = state.description
                )
                RecruitmentTextSection(
                    title = stringResource(R.string.recruitment_section_qualification),
                    content = state.qualification
                )
                RecruitmentTextSection(
                    title = stringResource(R.string.recruitment_section_preference),
                    content = state.preference
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (state.isDeleteDialogVisible) {
        RecruitmentDeleteDialog(
            onConfirm = onDeleteDialogDismiss,
            onDismiss = onDeleteDialogDismiss
        )
    }
}

@Composable
private fun RecruitmentDetailBottomAction(
    isAuthor: Boolean,
    isClosed: Boolean,
    modifier: Modifier = Modifier
) {
    val textRes = when {
        isAuthor -> R.string.recruitment_action_check_applicants
        isClosed -> R.string.recruitment_action_recruitment_closed
        else -> R.string.recruitment_action_apply
    }
    FilledButton(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 36.dp, vertical = 16.dp)
            .height(48.dp),
        text = stringResource(textRes),
        onClick = {},
        enabled = isAuthor || !isClosed,
        textStyle = RebrandKoinTheme.typography.bold15,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonColors(
            containerColor = RebrandKoinTheme.colors.primary500,
            contentColor = RebrandKoinTheme.colors.neutral0,
            disabledContainerColor = RebrandKoinTheme.colors.neutral400,
            disabledContentColor = RebrandKoinTheme.colors.neutral0
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentDetailScreenPreview() {
    RebrandKoinTheme {
        RecruitmentDetailScreenImpl(
            state = RecruitmentDetailState(
                id = 1,
                category = RecruitmentCategory.CONTEST,
                title = "AI 아이디어 공모전 팀원 모집",
                location = RecruitmentLocation.MIXED,
                activityStartDate = "2026.07.26",
                activityEndDate = "2026.08.07",
                dDay = 5,
                recruitmentType = RecruitmentType.ROLE_BASED,
                currentParticipants = 1,
                maxParticipants = 3,
                roles = persistentListOf(
                    RecruitmentRoleModel(id = 1, name = "프론트엔드", maxParticipants = 1, isClosed = true),
                    RecruitmentRoleModel(id = 2, name = "백엔드", maxParticipants = 1),
                    RecruitmentRoleModel(id = 3, name = "디자인", maxParticipants = 1)
                ),
                authorNickname = "코인이",
                description = "AI 아이디어 공모전에 함께 나갈 팀원을 찾습니다.",
                qualification = "2학년 이상이면서 학기 중 주 10시간 이상 프로젝트에 참여할 수 있는 사람\n참여율이 높은 사람",
                preference = "공모전 수상 경험이 있거나 비슷한 규모의 팀 프로젝트를 끝까지 완주해 본 경험이 있는 사람",
                createdAt = "2026.08.25"
            )
        )
    }
}
