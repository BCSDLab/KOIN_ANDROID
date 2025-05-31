package `in`.koreatech.koin.feature.club.ui.clublist

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubCategoryItem
import `in`.koreatech.koin.feature.club.component.KoinClubDropdown
import `in`.koreatech.koin.feature.club.component.KoinClubListItem
import `in`.koreatech.koin.feature.club.component.KoinClubMessageDialog
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.ParcelizeClubItem
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.model.clubSortType
import `in`.koreatech.koin.feature.club.ui.detail.component.snackbar.DetailSnackBar
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubListScreen(
    isClubCreated: Boolean = false,
    viewModel: ClubListViewModel = hiltViewModel(),
    navigateToCreateClub: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, viewModel, navigateToCreateClub)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isClubCreated) {
        if (isClubCreated) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.club_create_success_snackbar),
                duration = SnackbarDuration.Short
            )
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
            clubList = uiState.clubs,
            sortType = uiState.sortType,
            selectedCategoryId = uiState.categoryId,
            isDropdownExpanded = uiState.isDropdownExpanded,
            shouldShowClubCreateDialog = uiState.shouldShowClubCreateDialog,
            modifier = Modifier.padding(innerPadding),
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
            }
        )
    }
}

@Composable
fun ClubListScreenImpl(
    clubList: List<ParcelizeClubItem>,
    selectedCategoryId: Int?,
    sortType: ClubSort,
    isDropdownExpanded: Boolean,
    shouldShowClubCreateDialog: Boolean,
    modifier: Modifier = Modifier,
    onCategoryChange: (Int?) -> Unit = { },
    onSortTypeChange: (ClubSort) -> Unit = { },
    onDropdownExpandChange: (Boolean) -> Unit = { },
    navigateToCreateClub: () -> Unit = { },
    onShowClubCreateDialogChange: (Boolean) -> Unit = { }
) {
    if (shouldShowClubCreateDialog) {
        KoinClubMessageDialog(
            modifier = Modifier,
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

    LazyColumn(
        modifier = modifier.padding(horizontal = 24.dp)
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
                        onShowClubCreateDialogChange(true)
                    },
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                clubCategories.forEach {
                    KoinClubCategoryItem(
                        modifier = Modifier.weight(1f),
                        categoryName = stringResource(it.stringRes),
                        icon = painterResource(it.drawableRes),
                        isSelected = it.id == selectedCategoryId,
                        onClick = {
                            onCategoryChange(if (it.id == selectedCategoryId) null else it.id)
                        }
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                KoinClubDropdown(
                    text = stringResource(sortType.stringRes),
                    isDropdownExpanded = isDropdownExpanded,
                    items = clubSortType.map { stringResource(it.stringRes) }.toPersistentList(),
                    onDropdownExpandChange = onDropdownExpandChange,
                    onItemSelected = { index ->
                        onSortTypeChange(clubSortType[index])
                    }
                )
            }
        }

        items(clubList) {
            KoinClubListItem(
                id = it.id,
                name = it.name,
                category = it.category,
                likes = it.likes,
                logoUrl = it.imageUrl,
                modifier = Modifier.padding(vertical = 12.dp),
                onClick = {
                    // TODO
                }
            )
        }

        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

fun handleSideEffect(
    sideEffect: ClubListSideEffect,
    viewModel: ClubListViewModel,
    navigateToCreateClub: () -> Unit = { }
) {
    when (sideEffect) {
        is ClubListSideEffect.RefreshClubs -> {
            viewModel.getClubs()
        }

        ClubListSideEffect.NavigateToCreateClub -> navigateToCreateClub()
    }
}

@Preview(showBackground = true)
@Composable
fun ClubListScreenPreview() {
    ClubListScreenImpl(
        clubList = emptyList(),
        selectedCategoryId = 1,
        sortType = ClubSort.NONE,
        isDropdownExpanded = false,
        shouldShowClubCreateDialog = false
    )
}
