package `in`.koreatech.koin.feature.recruitment.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProfile
import `in`.koreatech.koin.feature.recruitment.ui.profile.component.ProfileEmptyState
import `in`.koreatech.koin.feature.recruitment.ui.profile.component.ProfileSummaryCard
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToMyRecruitment: () -> Unit = {},
    onNavigateToMyAppliedRecruitment: () -> Unit = {},
    onNavigateToProfileCreate: (isEditMode: Boolean) -> Unit = {}
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ProfileSideEffect.NavigateUp -> onNavigateUp()
            ProfileSideEffect.NavigateToMyRecruitment -> onNavigateToMyRecruitment()
            ProfileSideEffect.NavigateToMyAppliedRecruitment -> onNavigateToMyAppliedRecruitment()
            is ProfileSideEffect.NavigateToProfileCreate -> onNavigateToProfileCreate(sideEffect.isEditMode)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = RebrandKoinTheme.colors.neutral50,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_profile_title),
                onNavigationIconClick = onNavigateUp,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RebrandKoinTheme.colors.neutral50
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ProfileScreenImpl(
            state = state,
            modifier = Modifier.padding(innerPadding),
            onMyRecruitmentClick = viewModel::onMyRecruitmentClick,
            onMyAppliedRecruitmentClick = viewModel::onMyAppliedRecruitmentClick,
            onCreateProfileClick = viewModel::onCreateProfileClick,
            onEditProfileClick = viewModel::onEditProfileClick
        )
    }
}

@Composable
private fun ProfileScreenImpl(
    state: ProfileState,
    modifier: Modifier = Modifier,
    onMyRecruitmentClick: () -> Unit = {},
    onMyAppliedRecruitmentClick: () -> Unit = {},
    onCreateProfileClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (val loadState = state.loadState) {
            is ProfileLoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(vertical = 24.dp)
                ) {
                    CircularProgressIndicator(color = RebrandKoinTheme.colors.primary500)
                }
            }

            is ProfileLoadState.NotFound -> {
                ProfileEmptyState(onCreateProfileClick = onCreateProfileClick)
            }

            is ProfileLoadState.Loaded -> {
                ProfileSummaryCard(
                    profile = loadState.profile,
                    onEditClick = onEditProfileClick
                )
            }

            is ProfileLoadState.Error -> {
                Text(
                    text = loadState.message.orEmpty(),
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral500,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                )
            }
        }

        ProfileLinkItem(
            iconRes = R.drawable.ic_my_recruitment,
            title = stringResource(R.string.recruitment_profile_go_to_my_recruitment),
            description = stringResource(R.string.recruitment_profile_go_to_my_recruitment_description),
            onClick = onMyRecruitmentClick
        )
        ProfileLinkItem(
            iconRes = R.drawable.ic_my_applied_recruitment,
            title = stringResource(R.string.recruitment_profile_go_to_my_applied_recruitment),
            description = stringResource(R.string.recruitment_profile_go_to_my_applied_recruitment_description),
            onClick = onMyAppliedRecruitmentClick
        )

        Spacer(
            modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars)
        )
    }
}

@Composable
private fun ProfileLinkItem(
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral0, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(0.5.dp, RebrandKoinTheme.colors.neutral400, CircleShape)
                .background(RebrandKoinTheme.colors.neutral0, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.primary500,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.medium16,
                color = RebrandKoinTheme.colors.neutral800
            )
            Text(
                text = description,
                style = RebrandKoinTheme.typography.regular14,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenEmptyPreview() {
    RebrandKoinTheme {
        ProfileScreenImpl(state = ProfileState(loadState = ProfileLoadState.NotFound))
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenWithProfilePreview() {
    RebrandKoinTheme {
        ProfileScreenImpl(
            state = ProfileState(
                loadState = ProfileLoadState.Loaded(
                    RecruitmentProfile(
                        nickname = "BCSD",
                        department = "컴퓨터공학부",
                        studentId = "2023100000"
                    )
                )
            )
        )
    }
}
