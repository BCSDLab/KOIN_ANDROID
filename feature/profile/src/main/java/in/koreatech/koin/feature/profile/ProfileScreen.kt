package `in`.koreatech.koin.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinMainTopBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.profile.component.ProfileCard
import `in`.koreatech.koin.feature.profile.component.ProfileTimetable
import `in`.koreatech.koin.feature.profile.model.ProfileTimetableLecture
import kotlinx.collections.immutable.ImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSetting: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onRequestLogout: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val navigator = rememberNavigator()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ProfileSideEffect.LogoutSuccess -> onRequestLogout()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshTimetable()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8FA))
                .padding(paddingValues),
            uiState = uiState,
            onAuthClick = {
                if (uiState.isLoggedIn) {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Profile.HOME_LOGOUT, "로그아웃")
                    viewModel.onLogoutClick()
                } else {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Profile.HOME_LOGIN, "로그인")
                    navigator.navigateToSignIn(context, DEEPLINK_MAIN_PROFILE).let { context.startActivity(it) }
                }
            },
            onSettingClick = {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Profile.HOME_SETTINGS, "설정")
                onNavigateToSetting()
            },
            onNotificationClick = {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.NOTIFICATION, "알림 아이콘")
                onNavigateToNotification()
            },
            onTimetableClick = {
                EventLogger.logCampusClickEvent(AnalyticsConstant.Label.Profile.HOME_TIMETABLE, "내 시간표")
                navigator.navigateToTimetable(context, isAnonymous = !uiState.isLoggedIn)
                    .let { context.startActivity(it) }
            }
        )
    }
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileState,
    modifier: Modifier = Modifier,
    onAuthClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onTimetableClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        HeaderSection(
            isNewNotificationReceived = uiState.isNewNotificationReceived,
            onNotificationClick = onNotificationClick
        )

        ProfileCard(
            modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp),
            isLoggedIn = uiState.isLoggedIn,
            name = uiState.name,
            studentNumber = uiState.studentNumber,
            onAuthClick = onAuthClick,
            onSettingClick = onSettingClick
        )

        TimetableSection(
            lectures = uiState.timetable,
            onTimetableClick = onTimetableClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HeaderSection(
    isNewNotificationReceived: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {}
) {
    KoinMainTopBar(
        modifier = modifier,
        isNewNotificationReceived = isNewNotificationReceived,
        onNotificationClick = onNotificationClick
    )
}

@Composable
private fun TimetableSection(
    lectures: ImmutableList<ProfileTimetableLecture>,
    modifier: Modifier = Modifier,
    onTimetableClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_timetable_section),
            style = RebrandKoinTheme.typography.bold18
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, Color(0xFFE6E6E6), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(RebrandKoinTheme.colors.neutral0)
                .clickable { onTimetableClick() }
                .padding(16.dp)
        ) {
            ProfileTimetable(lectures = lectures)
        }
    }
}
