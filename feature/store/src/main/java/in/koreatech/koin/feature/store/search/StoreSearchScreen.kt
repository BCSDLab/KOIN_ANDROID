package `in`.koreatech.koin.feature.store.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.LocalDeliveryDeveloperOption
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.model.LocalShopSearchResult
import `in`.koreatech.koin.feature.store.search.component.SearchBar
import kotlinx.coroutines.flow.debounce
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreSearchViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit = { },
    onBackPressed: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.searchQuery }
            .debounce(300L)
            .collect {
                viewModel.onSearch()
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        KoinStoreTopAppBar(
            title = stringResource(R.string.store_title_home_search),
            onNavigationIconClick = {
                onBackPressed()
            },
            actions = {
                if (!LocalDeliveryDeveloperOption.current) return@KoinStoreTopAppBar
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.store_detail_background)
            )
        )

        StoreSearchScreen(
            query = uiState.searchQuery,
            searchResults = uiState.searchResults,
            onDismiss = onBackPressed,
            navigateToDetail = navigateToDetail,
            onQueryChange = viewModel::updateSearchQuery
        )
    }
}

@Composable
private fun StoreSearchScreen(
    query: String,
    searchResults: List<LocalShopSearchResult>,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit = {},
    navigateToDetail: (Int) -> Unit = { },
    onDismiss: () -> Unit = { }
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            query = query,
            onQueryChange = onQueryChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (searchResults.isEmpty()) {
                        RebrandKoinTheme.colors.neutral800.copy(alpha = 0.7f)
                    } else {
                        Color.Unspecified
                    }
                )
                .padding(horizontal = 16.dp)
        ) {
            items(searchResults) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToDetail(it.shopId)
                            EventLogger.logClickEvent(
                                EventAction.BUSINESS,
                                AnalyticsConstant.Label.SHOP_CATEGORIES_SEARCH_CLICK,
                                it.keyword
                            )
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(if (it.isShop) R.drawable.ic_store_search else R.drawable.ic_search_related_menu),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    BasicText(
                        text = it.keyword,
                        style = RebrandKoinTheme.typography.regular14.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (it.isShop) {
                        Image(
                            modifier = Modifier
                                .size(16.dp)
                                .noRippleClickable {
                                    onQueryChange(it.keyword)
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.ic_store_search_select),
                            contentDescription = null
                        )
                    }
                }
                HorizontalDivider(
                    color = RebrandKoinTheme.colors.neutral300
                )
            }

            item {
                Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
            }
        }
    }

    BackHandler { onDismiss() }
}
