package `in`.koreatech.koin.feature.recruitment.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.snackbar.CustomSnackBarHost
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.notification.component.RecruitmentNotificationItem
import `in`.koreatech.koin.feature.recruitment.ui.notification.component.RecruitmentNotificationMenuButton
import `in`.koreatech.koin.feature.recruitment.ui.notification.model.RecruitmentNotification
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun RecruitmentNotificationScreen(
    onBack: () -> Unit = {},
    onNavigateToApplicantManagement: (Long) -> Unit = {},
    viewModel: RecruitmentNotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            RecruitmentNotificationSideEffect.Error -> {
                snackbarHostState.showSnackbar(context.getString(R.string.recruitment_notification_error))
            }

            is RecruitmentNotificationSideEffect.NavigateToApplicantManagement -> {
                onNavigateToApplicantManagement(sideEffect.recruitmentId)
            }

            RecruitmentNotificationSideEffect.Deleted -> {
                snackbarHostState.showSnackbar(context.getString(R.string.recruitment_notification_deleted))
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_notification_title),
                onNavigationIconClick = onBack,
                actions = {
                    RecruitmentNotificationMenuButton(
                        onMarkAllAsRead = viewModel::readAllNotifications,
                        onDeleteAll = viewModel::deleteAllNotifications
                    )
                }
            )
        },
        bottomBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                text = stringResource(R.string.recruitment_notification_auto_delete_info),
                style = RebrandKoinTheme.typography.regular14,
                color = Color(0xFFCACACA),
                textAlign = TextAlign.Center
            )
        },
        snackbarHost = {
            CustomSnackBarHost(
                hotState = snackbarHostState,
                background = Color(0xCC727272)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        RecruitmentNotificationScreenImpl(
            modifier = Modifier.padding(paddingValues),
            listState = listState,
            notifications = uiState.notifications,
            isLoading = uiState.isLoading,
            isLoadingMore = uiState.isLoadingMore,
            hasMore = uiState.currentPage < uiState.totalPage,
            onNotificationClick = viewModel::onNotificationClick,
            onLoadMore = viewModel::loadMoreNotifications
        )
    }
}

@Composable
private fun RecruitmentNotificationScreenImpl(
    listState: LazyListState,
    notifications: ImmutableList<RecruitmentNotification>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onNotificationClick: (RecruitmentNotification) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(listState, hasMore, isLoadingMore, notifications.size) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { if (hasMore && !isLoadingMore) onLoadMore() }
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = RebrandKoinTheme.colors.primary500)
        }
    }

    if (notifications.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_bbiko_sleep),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.recruitment_notification_empty),
                style = RebrandKoinTheme.typography.medium18.merge(
                    color = RebrandKoinTheme.colors.primary500
                )
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        items(notifications, key = { it.id }) { notification ->
            RecruitmentNotificationItem(
                modifier = Modifier.animateItem(),
                category = notification.category,
                senderNickname = notification.senderNickname,
                content = notification.content,
                timestamp = notification.timestamp,
                isRead = notification.isRead,
                onClick = remember(notification.id) { { onNotificationClick(notification) } }
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
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = RebrandKoinTheme.colors.primary500
                    )
                }
            }
        }
    }
}

private const val LOAD_MORE_THRESHOLD = 3
