package `in`.koreatech.koin.feature.club.ui.clublist

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.ParcelizeClubItem
import `in`.koreatech.koin.feature.club.model.clubCategories
import `in`.koreatech.koin.feature.club.model.clubSortType
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubListScreen(
    viewModel: ClubListViewModel = hiltViewModel(),
    navigateToClubDetail: (Int) -> Unit = { _ -> }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, viewModel)
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
        // Let's try to draw content behind the system bars
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        ClubListScreenImpl(
            clubList = uiState.clubs,
            sortType = uiState.sortType,
            selectedCategoryId = uiState.categoryId,
            isDropdownExpanded = uiState.isDropdownExpanded,
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
            navigateToClubDetail = navigateToClubDetail
        )
    }
}

@Composable
fun ClubListScreenImpl(
    clubList: List<ParcelizeClubItem>,
    selectedCategoryId: Int?,
    sortType: ClubSort,
    isDropdownExpanded: Boolean,
    modifier: Modifier = Modifier,
    onCategoryChange: (Int?) -> Unit = { },
    onSortTypeChange: (ClubSort) -> Unit = { },
    onDropdownExpandChange: (Boolean) -> Unit = { },
    navigateToClubDetail: (Int) -> Unit = { _ -> }
) {
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
                    onClick = {},
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
                onClick = { id ->
                    navigateToClubDetail(id)
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
    viewModel: ClubListViewModel
) {
    when (sideEffect) {
        is ClubListSideEffect.RefreshClubs -> {
            viewModel.getClubs()
        }
    }
}

@Preview
@Composable
fun ClubListScreenPreview() {
    ClubListScreen()
}
