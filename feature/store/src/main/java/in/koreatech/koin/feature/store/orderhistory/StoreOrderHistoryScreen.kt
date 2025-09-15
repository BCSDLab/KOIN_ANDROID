package `in`.koreatech.koin.feature.store.orderhistory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.SearchBarFake
import `in`.koreatech.koin.feature.store.orderhistory.component.OrderHistoryCard
import `in`.koreatech.koin.feature.store.orderhistory.component.OrderHistoryFilterBottomSheet
import `in`.koreatech.koin.feature.store.orderhistory.component.OrderInProgressCard
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderHistoryTabs
import `in`.koreatech.koin.feature.store.orderhistory.model.OrderHistoryData
import `in`.koreatech.koin.feature.store.orderhistory.model.OrderInProgressData
import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters
import `in`.koreatech.koin.feature.store.search.component.SearchBar
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    viewModel: StoreOrderHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        KoinStoreTopAppBar(
            title = stringResource(R.string.order_history),
            onNavigationIconClick = {
            },
            actions = {
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = {}) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                            contentDescription = null
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.store_detail_background)
            )
        )

        TabRow(
            selectedTabIndex = uiState.selectedIndex,
            containerColor = colorResource(id = R.color.store_detail_background),
            tabs = {
                OrderHistoryTabs.entries.forEachIndexed { index, tabs ->
                    Tab(
                        selected = uiState.selectedIndex == index,
                        selectedContentColor = RebrandKoinTheme.colors.primary500,
                        unselectedContentColor = RebrandKoinTheme.colors.neutral500,
                        onClick = { viewModel.updateSelectedTab(index) }
                    ) {
                        BasicText(
                            modifier = Modifier.padding(vertical = 12.dp),
                            style = RebrandKoinTheme.typography.medium16.merge(
                                color = if (uiState.selectedIndex == index) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral500
                            ),
                            text = stringResource(tabs.stringRes)
                        )
                    }
                }
            }
        )

        when (uiState.selectedIndex) {
            0 -> {
                if (uiState.orderHistories.isEmpty()) {
                    OrderHistoryEmptyScreen()
                } else {
                    OrderHistoryScreen(
                        showFilters = uiState.showFilters,
                        filters = uiState.filters,
                        isSearching = uiState.isSearching,
                        searchQuery = uiState.searchQuery,
                        orderHistories = uiState.orderHistories,
                        modifier = Modifier,
                        updateShowFilters = viewModel::updateShowFilters,
                        updateFilters = viewModel::updateFilters,
                        updateIsSearching = viewModel::updateIsSearching,
                        updateSearchQuery = viewModel::updateSearchQuery
                    )
                }
            }

            1 -> {
                if (uiState.orderInProgress.isEmpty()) {
                    OrderInProgressEmptyScreen()
                } else {
                    OrderInProgressScreen(
                        orderInProgress = uiState.orderInProgress
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryScreen(
    showFilters: Boolean,
    filters: StoreOrderHistoryFilters,
    isSearching: Boolean,
    searchQuery: String,
    orderHistories: List<OrderHistoryData>,
    modifier: Modifier = Modifier,
    updateIsSearching: (Boolean) -> Unit = {},
    updateShowFilters: (Boolean) -> Unit = {},
    updateFilters: (StoreOrderHistoryFilters) -> Unit = {},
    updateSearchQuery: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        if (isSearching) {
            BackHandler {
                updateIsSearching(false)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.7f))
                    .clickable {
                        updateIsSearching(false)
                    }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBarAndFilter(
                modifier = Modifier.zIndex(2f),
                showFilters = showFilters,
                filters = filters,
                isSearching = isSearching,
                searchQuery = searchQuery,
                updateFilters = updateFilters,
                updateShowFilters = updateShowFilters,
                updateIsSearching = updateIsSearching,
                updateSearchQuery = updateSearchQuery
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orderHistories) {
                    OrderHistoryCard(
                        orderHistoryData = it
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderHistoryEmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bbiko_sleep),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicText(
            text = stringResource(R.string.no_order_history),
            style = RebrandKoinTheme.typography.medium18.merge(
                color = RebrandKoinTheme.colors.primary500
            )
        )
    }
}

@Composable
private fun OrderInProgressScreen(
    orderInProgress: List<OrderInProgressData>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(orderInProgress) {
            OrderInProgressCard(it)
        }
    }
}

@Composable
private fun OrderInProgressEmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_bbiko_sleep),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicText(
            text = stringResource(R.string.no_ongoing_orders),
            style = RebrandKoinTheme.typography.medium18.merge(
                color = RebrandKoinTheme.colors.primary500
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        FilledButton(
            shape = RebrandKoinTheme.shapes.small,
            onClick = {},
            text = stringResource(R.string.goto_order_history),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.neutral500
            ),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        )
    }
}

@Composable
private fun SearchBarAndFilter(
    showFilters: Boolean,
    filters: StoreOrderHistoryFilters,
    isSearching: Boolean,
    searchQuery: String,
    modifier: Modifier = Modifier,
    updateIsSearching: (Boolean) -> Unit = {},
    updateShowFilters: (Boolean) -> Unit = {},
    updateFilters: (StoreOrderHistoryFilters) -> Unit = {},
    updateSearchQuery: (String) -> Unit = {}
) {
    if (showFilters) {
        OrderHistoryFilterBottomSheet(
            filters = filters,
            updateFilters = updateFilters,
            onClose = {
                updateShowFilters(false)
            }
        )
    }

    Column(
        modifier = modifier
            .background(colorResource(id = R.color.store_detail_background))
            .padding(vertical = 16.dp)
    ) {
        if (isSearching) {
            SearchBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                query = searchQuery,
                onQueryChange = updateSearchQuery
            )
        } else {
            SearchBarFake(
                modifier = Modifier.padding(horizontal = 24.dp),
                query = searchQuery
            ) {
                updateIsSearching(true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            if (filters.orderHistoryPeriod != null || filters.orderType != null || filters.orderStatusFilter != null) {
                KoinStoreChip(
                    modifier = modifier,
                    text = stringResource(R.string.orders_chip_reset),
                    chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        textColor = RebrandKoinTheme.colors.neutral500
                    ),
                    trailingIcon = painterResource(R.drawable.ic_process),
                    trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
                        iconColor = RebrandKoinTheme.colors.neutral500
                    ),
                    onClick = { updateFilters(StoreOrderHistoryFilters()) }
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            KoinStoreChip(
                modifier = modifier,
                text = stringResource(if (filters.orderHistoryPeriod == null) R.string.order_history_period_none else filters.orderHistoryPeriod.stringRes),
                chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                    containerColor = if (filters.orderHistoryPeriod == null) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500,
                    textColor = if (filters.orderHistoryPeriod == null) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0
                ),
                trailingIcon = painterResource(R.drawable.ic_store_arrow_down),
                trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
                    iconColor = if (filters.orderHistoryPeriod == null) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0
                ),
                onClick = { updateShowFilters(true) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            KoinStoreChip(
                modifier = modifier,
                text = stringResource(
                    R.string.bullet_separator,
                    if (filters.orderType == null) stringResource(R.string.order_type_none) else stringResource(filters.orderType.stringRes),
                    if (filters.orderStatusFilter == null) stringResource(R.string.order_status_none) else stringResource(filters.orderStatusFilter.stringRes)
                ),
                chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                    containerColor = if (filters.orderType == null || filters.orderStatusFilter == null) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500,
                    textColor = if (filters.orderType == null || filters.orderStatusFilter == null) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0
                ),
                trailingIcon = painterResource(R.drawable.ic_store_arrow_down),
                trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
                    iconColor = if (filters.orderType == null || filters.orderStatusFilter == null) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0
                ),
                onClick = { updateShowFilters(true) }
            )
        }
    }
}

@Preview
@Composable
private fun OrderHistoryScreenPreview() {
    OrderHistoryScreen(
        searchQuery = "",
        isSearching = false,
        showFilters = false,
        orderHistories = emptyList(),
        filters = StoreOrderHistoryFilters(),
        modifier = Modifier
    )
}
