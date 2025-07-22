package `in`.koreatech.koin.feature.club.ui.clublist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.constant.LOGIN_ACTIVITY_URL
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubCategoryItem
import `in`.koreatech.koin.feature.club.component.KoinClubDropdown
import `in`.koreatech.koin.feature.club.component.KoinClubExtraSmallDialog
import `in`.koreatech.koin.feature.club.component.KoinClubListItem
import `in`.koreatech.koin.feature.club.component.KoinClubMessageDialog
import `in`.koreatech.koin.feature.club.component.KoinClubSearchBar
import `in`.koreatech.koin.feature.club.component.KoinClubSwitch
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.ParcelizeClubItem
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.model.clubSortType
import `in`.koreatech.koin.feature.club.model.clubSortTypeWithRecruitment
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.snackbar.DetailSnackBar
import `in`.koreatech.koin.feature.club.utils.ellipsize
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubListScreen(
    isClubCreated: Boolean = false,
    viewModel: ClubListViewModel = hiltViewModel(),
    navigateToCreateClub: () -> Unit = { },
    navigateToClubDetail: (Int) -> Unit = { _ -> },
    resetClubCreatedState: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, viewModel, context, navigateToCreateClub)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isInitialized) {
        if (!uiState.isInitialized) return@LaunchedEffect
        viewModel.getClubs()
    }

    LaunchedEffect(isClubCreated) {
        if (isClubCreated) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.club_create_success_snackbar),
                duration = SnackbarDuration.Short
            )
            resetClubCreatedState()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.searchKeyword }
            .debounce(300L)
            .collectLatest {
                viewModel.getClubs()
            }
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.club_list_title),
                onNavigationIconClick = {
                    // This is top navigation
                    (context as Activity).finish()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    DetailSnackBar(
                        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
                        message = data.visuals.message,
                        label = data.visuals.actionLabel,
                        onLabelClick = { data.performAction() }
                    )
                }
            )
        },
        // Let's try to draw content behind the system bars
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ClubListScreenImpl(
            isLoading = uiState.isLoading,
            clubList = uiState.clubs,
            sortType = uiState.sortType,
            selectedCategoryId = uiState.categoryId,
            isDropdownExpanded = uiState.isDropdownExpanded,
            shouldShowClubCreateDialog = uiState.shouldShowClubCreateDialog,
            shouldShowLoginDialog = uiState.shouldShowLoginDialog,
            isAnonymous = uiState.isAnonymous,
            searchKeyword = uiState.searchKeyword,
            isRecruiting = uiState.isRecruiting,
            modifier = Modifier.padding(innerPadding),
            onSearchKeywordChange = {
                viewModel.updateSearchKeyword(it)
            },
            onCategoryChange = { categoryId ->
                viewModel.updateCategoryId(categoryId)
            },
            onSortTypeChange = { sortType ->
                viewModel.updateSortType(sortType)
            },
            onDropdownExpandChange = { isExpanded ->
                viewModel.updateDropdownExpanded(isExpanded)
            },
            navigateToCreateClub = {
                viewModel.navigateToCreateClub()
            },
            onShowClubCreateDialogChange = { shouldShow ->
                viewModel.updateShowClubCreateDialog(shouldShow)
            },
            navigateToClubDetail = navigateToClubDetail,
            onShowLoginDialogChange = { shouldShow ->
                viewModel.updateShowLoginDialog(shouldShow)
            },
            onLikeClick = { clubId ->
                viewModel.changeClubLike(clubId)
            },
            onRecruitmentChange = { isRecruiting ->
                viewModel.updateRecruiting(isRecruiting)
            },
            navigateToLogin = {
                Intent(Intent.ACTION_VIEW, LOGIN_ACTIVITY_URL.toUri()).let {
                    context.startActivity(it)
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClubListScreenImpl(
    isLoading: Boolean,
    clubList: List<ParcelizeClubItem>,
    selectedCategoryId: Int?,
    sortType: ClubSort,
    isDropdownExpanded: Boolean,
    shouldShowClubCreateDialog: Boolean,
    shouldShowLoginDialog: Boolean,
    isAnonymous: Boolean,
    searchKeyword: String,
    isRecruiting: Boolean,
    modifier: Modifier = Modifier,
    onSearchKeywordChange: (String) -> Unit = { },
    onCategoryChange: (Int?) -> Unit = { },
    onSortTypeChange: (ClubSort) -> Unit = { },
    onDropdownExpandChange: (Boolean) -> Unit = { },
    navigateToCreateClub: () -> Unit = { },
    onShowClubCreateDialogChange: (Boolean) -> Unit = { },
    onShowLoginDialogChange: (Boolean) -> Unit = { _ -> },
    onLikeClick: (Int) -> Unit = { _ -> },
    onRecruitmentChange: (Boolean) -> Unit = { _ -> },
    navigateToClubDetail: (Int) -> Unit = { _ -> },
    navigateToLogin: () -> Unit = { }
) {
    val context = LocalContext.current

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (shouldShowClubCreateDialog) {
        KoinClubMessageDialog(
            title = stringResource(R.string.club_list_create_dialog_title),
            onPositive = {
                onShowClubCreateDialogChange(false)
                navigateToCreateClub()
            },
            onDismissRequest = {
                onShowClubCreateDialogChange(false)
            },
            content = {
                Text(
                    text = stringResource(R.string.club_list_create_dialog_content)
                )
            }
        )
    }

    if (shouldShowLoginDialog) {
        KoinClubExtraSmallDialog(
            title = stringResource(R.string.detail_dialog_login_title),
            description = stringResource(R.string.detail_dialog_login_description),
            positiveButtonText = stringResource(id = R.string.detail_dialog_login_positive),
            onPositive = {
                navigateToLogin()
                onShowLoginDialogChange(false)
            },
            onNegative = { onShowLoginDialogChange(false) }
        )
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.club_list_list),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.primary500
                )

                Spacer(modifier = Modifier.weight(1f))

                FilledButton(
                    text = stringResource(R.string.club_list_create_club),
                    textStyle = KoinTheme.typography.medium12,
                    onClick = {
                        if (isAnonymous) {
                            onShowLoginDialogChange(true)
                            return@FilledButton
                        }
                        EventLogger.logCampusClickEvent(
                            AnalyticsConstant.Label.Club.MAIN_CLUB_CREATE,
                            "생성하기"
                        )
                        onShowClubCreateDialogChange(true)
                    },
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
                )
            }
        }

        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterHorizontally)
            ) {
                clubCategories.forEach {
                    KoinClubCategoryItem(
                        categoryName = stringResource(it.stringRes),
                        icon = painterResource(it.drawableRes),
                        isSelected = it.id == selectedCategoryId,
                        onClick = {
                            if (it.id != selectedCategoryId) {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.Club.MAIN_CLUB_CATEGORY,
                                    context.getString(it.stringRes)
                                )
                            }
                            onCategoryChange(if (it.id == selectedCategoryId) null else it.id)
                        }
                    )
                }
            }
        }

        item {
            KoinClubSearchBar(
                modifier = Modifier.padding(top = 16.dp),
                value = searchKeyword,
                hint = stringResource(R.string.club_list_search_hint),
                onValueChange = onSearchKeywordChange
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.club_list_count, clubList.size),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral600
                )

                Spacer(modifier = Modifier.weight(1f))

                KoinClubDropdown(
                    text = stringResource(sortType.stringRes),
                    isDropdownExpanded = isDropdownExpanded,
                    items = (if (isRecruiting) clubSortTypeWithRecruitment else clubSortType).map { stringResource(it.stringRes) }.toPersistentList(),
                    onDropdownExpandChange = onDropdownExpandChange,
                    onItemSelected = { index ->
                        onSortTypeChange((if (isRecruiting) clubSortTypeWithRecruitment else clubSortType)[index])
                    }
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.club_list_recruiting),
                    style = KoinTheme.typography.regular14,
                    color = KoinTheme.colors.neutral800
                )

                Spacer(modifier = Modifier.width(12.dp))

                KoinClubSwitch(
                    checked = isRecruiting,
                    onCheckedChange = onRecruitmentChange
                )
            }
        }

        clubItems(
            clubList = clubList,
            isAnonymous = isAnonymous,
            isRecruiting = isRecruiting,
            searchKeyword = searchKeyword,
            navigateToClubDetail = navigateToClubDetail,
            onShowLoginDialogChange = onShowLoginDialogChange,
            onLikeClick = onLikeClick
        )

        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

inline fun LazyListScope.clubItems(
    clubList: List<ParcelizeClubItem>,
    isAnonymous: Boolean,
    isRecruiting: Boolean = false,
    searchKeyword: String = "",
    crossinline navigateToClubDetail: (Int) -> Unit = { _ -> },
    crossinline onShowLoginDialogChange: (Boolean) -> Unit = { _ -> },
    crossinline onLikeClick: (Int) -> Unit = { _ -> }
) = if (searchKeyword.isNotEmpty() && clubList.isEmpty()) {
    item {
        Box(
            modifier = Modifier.aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.club_list_search_not_found, searchKeyword.ellipsize()),
                style = KoinTheme.typography.regular18,
                color = KoinTheme.colors.neutral500,
                textAlign = TextAlign.Center
            )
        }
    }
} else if (isRecruiting && clubList.isEmpty()) {
    item {
        Box(
            modifier = Modifier.aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.club_list_recruiting_not_found),
                style = KoinTheme.typography.regular18,
                color = KoinTheme.colors.neutral500,
                textAlign = TextAlign.Center
            )
        }
    }
} else {
    itemsIndexed(clubList) { index, it ->
        KoinClubListItem(
            id = it.id,
            name = it.name,
            category = it.category,
            likes = it.likes,
            logoUrl = it.imageUrl,
            isLiked = it.isLiked,
            isLikeHidden = it.isLikeHidden,
            recruitmentInfo = it.recruitmentInfo,
            modifier = Modifier.padding(
                top = if (index == 0) 0.dp else 12.dp,
                bottom = if (index == clubList.lastIndex) 12.dp else 0.dp
            ),
            onClick = { id ->
                EventLogger.logCampusClickEvent(
                    AnalyticsConstant.Label.Club.MAIN_SELECT_CLUB,
                    it.name
                )
                navigateToClubDetail(id)
            },
            onLikeClick = { id ->
                if (isAnonymous) {
                    onShowLoginDialogChange(true)
                    return@KoinClubListItem
                }
                onLikeClick(id)
            }
        )
    }
}

fun handleSideEffect(
    sideEffect: ClubListSideEffect,
    viewModel: ClubListViewModel,
    context: Context,
    navigateToCreateClub: () -> Unit = { }
) {
    when (sideEffect) {
        is ClubListSideEffect.RefreshClubs -> {
            viewModel.getClubs()
        }

        ClubListSideEffect.NavigateToCreateClub -> navigateToCreateClub()
        ClubListSideEffect.ClubDislikeFailed -> Toast.makeText(context, R.string.club_list_dislike_failed, Toast.LENGTH_SHORT).show()
        ClubListSideEffect.ClubLikeFailed -> Toast.makeText(context, R.string.club_list_like_failed, Toast.LENGTH_SHORT).show()
        ClubListSideEffect.ClubsFetchFailed -> Toast.makeText(context, R.string.club_list_fetch_failed, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun ClubListScreenPreview() {
    ClubListScreenImpl(
        isLoading = false,
        clubList = emptyList(),
        selectedCategoryId = 1,
        sortType = ClubSort.CREATED_AT_ASC,
        isDropdownExpanded = false,
        shouldShowClubCreateDialog = false,
        shouldShowLoginDialog = false,
        isAnonymous = true,
        searchKeyword = "",
        isRecruiting = false
    )
}
