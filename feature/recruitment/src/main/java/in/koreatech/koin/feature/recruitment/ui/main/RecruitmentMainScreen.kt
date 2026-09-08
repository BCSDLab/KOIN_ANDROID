package `in`.koreatech.koin.feature.recruitment.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.notification.FirebaseMessagingType
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.navigation.UNREAD_NOTIFICATION_STATE_UPDATE
import `in`.koreatech.koin.feature.recruitment.ui.component.rememberRecruitmentPaginationListState
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentAppliedFilterChipGroup
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentChip
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentChipDefaults
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentFilterBottomSheet
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentMainItem
import `in`.koreatech.koin.feature.recruitment.ui.main.component.RecruitmentSearchField
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RecruitmentMainScreen(
    navController: NavController,
    viewModel: RecruitmentMainViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {}
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    val unreadNotificationStateUpdated = navController.currentBackStackEntry?.savedStateHandle?.getStateFlow(UNREAD_NOTIFICATION_STATE_UPDATE, false)?.collectAsState()

    LaunchedEffect(unreadNotificationStateUpdated?.value) {
        if (unreadNotificationStateUpdated?.value == true) {
            viewModel.getNotificationCount()
            navController.currentBackStackEntry?.savedStateHandle?.set(UNREAD_NOTIFICATION_STATE_UPDATE, false)
        }
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.getStringExtra("NOTIFICATION_TYPE") == FirebaseMessagingType.TEAM_RECRUITMENT.name) {
                    viewModel.getNotificationCount()
                }
            }
        }

        val filter = IntentFilter("${context.packageName}.ACTION_NOTIFICAION_RECEIVED")
        context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.fetchRecruitments()
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            RecruitmentMainSideEffect.ShowError ->
                ToastUtil.getInstance().makeShort(context.getString(R.string.recruitment_load_error))
        }
    }

    if (state.isFilterVisible) {
        RecruitmentFilterBottomSheet(
            state = state.pendingFilterState,
            onStatusClick = viewModel::selectPendingStatus,
            onSortClick = viewModel::selectPendingSort,
            onCategoryClick = viewModel::togglePendingCategory,
            onLocationClick = viewModel::selectPendingLocation,
            onReset = viewModel::resetPendingFilter,
            onApplyClick = viewModel::applyPendingFilter,
            onDismissRequest = { viewModel.updateFilterVisible(false) }
        )
    }

    RecruitmentMainScreenImpl(
        searchValue = state.searchValue,
        items = state.items,
        totalCount = state.totalCount,
        isRefreshing = state.isRefreshing,
        onRefresh = viewModel::fetchRecruitments,
        isLoadingMore = state.isLoadingMore,
        hasMore = state.currentPage < state.totalPage,
        isUnreadNotificationAvailable = state.isUnreadNotificationAvailable,
        onLoadMore = viewModel::loadMoreRecruitments,
        filterState = state.filterState,
        onSearchValueChange = viewModel::updateSearch,
        onFilterClick = { viewModel.updateFilterVisible(true) },
        onRemoveStatus = viewModel::removeStatusFilter,
        onRemoveCategory = viewModel::removeCategoryFilter,
        onRemoveLocation = viewModel::removeLocationFilter,
        onTopbarBackClick = onTopbarBackClick,
        onNotificationClick = onNotificationClick,
        onProfileClick = onProfileClick,
        onWriteClick = onWriteClick,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
@Composable
private fun RecruitmentMainScreenImpl(
    searchValue: String,
    items: ImmutableList<RecruitmentItemModel>,
    totalCount: Long,
    filterState: RecruitmentFilterState,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    isLoadingMore: Boolean = false,
    hasMore: Boolean = false,
    isUnreadNotificationAvailable: Boolean = false,
    onLoadMore: () -> Unit = {},
    onSearchValueChange: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onRemoveStatus: () -> Unit = {},
    onRemoveCategory: (RecruitmentCategory) -> Unit = {},
    onRemoveLocation: () -> Unit = {},
    onTopbarBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onWriteClick: () -> Unit = {},
    onItemClick: (Int) -> Unit = {}
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.recruitment_top_bar_title),
                textStyle = RebrandKoinTheme.typography.bold16,
                onNavigationIconClick = onTopbarBackClick,
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isUnreadNotificationAvailable) {
                                ImageVector.vectorResource(R.drawable.ic_rebrand_notification_dot)
                            } else {
                                ImageVector.vectorResource(R.drawable.ic_rebrand_notification)
                            },
                            contentDescription = stringResource(R.string.recruitment_notification_content_description),
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable(onClick = {
                                    EventLogger.logCampusClickEvent(
                                        AnalyticsConstant.Label.TeamRecruitment.NOTIFICATION,
                                        "알림"
                                    )
                                    onNotificationClick()
                                })
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_user_bold),
                            contentDescription = stringResource(R.string.recruitment_profile_content_description),
                            tint = RebrandKoinTheme.colors.neutral700,
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable(onClick = {
                                    EventLogger.logCampusClickEvent(
                                        AnalyticsConstant.Label.TeamRecruitment.PROFILE,
                                        "프로필"
                                    )
                                    onProfileClick()
                                })
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            RecruitmentChip(
                text = stringResource(R.string.recruitment_write_button),
                modifier = Modifier.padding(bottom = 24.dp, end = 8.dp),
                colors = RecruitmentChipDefaults.colors(
                    containerColor = RebrandKoinTheme.colors.primary400,
                    contentColor = RebrandKoinTheme.colors.neutral0
                ),
                textStyle = RebrandKoinTheme.typography.bold16,
                shape = RecruitmentChipDefaults.PillShape,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                trailingIconSize = 22.dp,
                trailingIconTint = Color.Unspecified,
                trailingIcon = ImageVector.vectorResource(R.drawable.ic_recruitment_edit),
                onClick = {
                    EventLogger.logCampusClickEvent(AnalyticsConstant.Label.TeamRecruitment.RECRUIT, "모집하기")
                    onWriteClick()
                }
            )
        },
        containerColor = RebrandKoinTheme.colors.neutral50
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RecruitmentSearchField(
                        value = searchValue,
                        onValueChange = onSearchValueChange,
                        modifier = Modifier.weight(1f)
                    )
                    RecruitmentChip(
                        text = stringResource(R.string.recruitment_filter),
                        modifier = Modifier.height(RecruitmentChipDefaults.PillHeight),
                        colors = RecruitmentChipDefaults.colors(
                            containerColor = RebrandKoinTheme.colors.neutral0,
                            contentColor = RebrandKoinTheme.colors.neutral600
                        ),
                        textStyle = RebrandKoinTheme.typography.regular12,
                        shape = RecruitmentChipDefaults.PillShape,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary100),
                        trailingIconSize = 21.dp,
                        trailingIconTint = Color.Unspecified,
                        trailingIcon = ImageVector.vectorResource(R.drawable.ic_recruitment_filter),
                        onClick = {
                            EventLogger.logCampusClickEvent(AnalyticsConstant.Label.TeamRecruitment.FILTER, "필터")
                            onFilterClick()
                        }
                    )
                }
                if (filterState.hasVisibleChips) {
                    Spacer(modifier = Modifier.height(12.dp))
                    RecruitmentAppliedFilterChipGroup(
                        filterState = filterState,
                        onRemoveStatus = onRemoveStatus,
                        onRemoveCategory = onRemoveCategory,
                        onRemoveLocation = onRemoveLocation
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.recruitment_total_count, totalCount),
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }

            val pullToRefreshState = rememberPullToRefreshState()
            PullToRefreshBox(
                modifier = Modifier.weight(1f),
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                state = pullToRefreshState,
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing,
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        color = RebrandKoinTheme.colors.primary500,
                        state = pullToRefreshState
                    )
                }
            ) {
                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        RecruitmentEmptyContent(modifier = Modifier.padding(bottom = 40.dp))
                    }
                } else {
                    val listState = rememberRecruitmentPaginationListState(
                        hasMore = hasMore,
                        isLoadingMore = isLoadingMore,
                        onLoadMore = onLoadMore
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items, key = { it.id }) { item ->
                            RecruitmentMainItem(
                                modifier = Modifier.animateItem(),
                                item = item,
                                onClick = {
                                    EventLogger.logCampusClickEvent(
                                        AnalyticsConstant.Label.TeamRecruitment.POST_SELECT,
                                        item.title
                                    )
                                    onItemClick(item.id)
                                }
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
            }
        }
    }
}

@Preview
@Composable
private fun RecruitmentMainScreenAppliedFilterPreview() {
    RebrandKoinTheme {
        RecruitmentMainScreenImpl(
            searchValue = "",
            totalCount = 1,
            items = persistentListOf(
                RecruitmentItemModel(
                    id = 1,
                    category = RecruitmentCategory.STUDY,
                    status = RecruitmentStatus.RECRUITING,
                    dDay = 13,
                    title = "2026 스터디 팀원 모집",
                    location = RecruitmentLocation.MIXED,
                    activityStartDate = "2026.07.26",
                    activityEndDate = "2026.08.07",
                    currentCount = 2,
                    maxCount = 3
                )
            ),
            filterState = RecruitmentFilterState(
                selectedStatus = RecruitmentStatus.RECRUITING,
                selectedCategories = persistentListOf(RecruitmentCategory.STUDY)
            )
        )
    }
}

@Preview
@Composable
private fun RecruitmentMainScreenEmptyPreview() {
    RebrandKoinTheme {
        RecruitmentMainScreenImpl(
            searchValue = "검색결과없음",
            totalCount = 0,
            items = persistentListOf(),
            filterState = RecruitmentFilterState()
        )
    }
}

@Composable
private fun RecruitmentEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_bbiko_sleep),
                contentDescription = null,
                modifier = Modifier.padding(start = 3.dp)
            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_sleep_effect),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(25.dp)
            )
        }
        Text(
            text = stringResource(R.string.recruitment_empty_result),
            style = RebrandKoinTheme.typography.regular14,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}
