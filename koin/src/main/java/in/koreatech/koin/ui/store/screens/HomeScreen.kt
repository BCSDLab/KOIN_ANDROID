package `in`.koreatech.koin.ui.store.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.component.topbar.StoreTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.ui.store.activity.StoreDetailActivity
import `in`.koreatech.koin.ui.store.components.AutoScrollingBanner
import `in`.koreatech.koin.ui.store.components.CategoryChips
import `in`.koreatech.koin.ui.store.components.SearchBar
import `in`.koreatech.koin.ui.store.components.SearchBarFake
import `in`.koreatech.koin.ui.store.components.SearchResultItem
import `in`.koreatech.koin.ui.store.components.StoreCard
import `in`.koreatech.koin.ui.store.components.StoreFilterBar
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    categoryId: Int,
    viewModel: StoreViewModel,
    onNavigationClick: () -> Unit,
    onCartClick: () -> Unit
) {
    var isSearchMode by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchQuery by viewModel.search.collectAsState()
    val searchRelated = viewModel.searchRelated.observeAsState().value
    val searchResults = searchRelated?.keywords ?: emptyList()
    val storeEvents by viewModel.storeEvents.observeAsState(emptyList())
    val categories by viewModel.storeCategoryList.observeAsState(emptyList())
    val selectedCategoryIndex by viewModel.categoryPosition.observeAsState(categoryId)
    val storeList by viewModel.stores.collectAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    fun exitSearchMode() {
        isSearchMode = false
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    BackHandler {
        if (isSearchMode) {
            exitSearchMode()
        } else {
            onNavigationClick()
        }
    }

    LaunchedEffect(isSearchMode) {
        if (isSearchMode) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(categoryId) {
        viewModel.setCategory(categoryId)
        viewModel.refreshStores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KoinTheme.colors.info50)
    ) {
        StoreTopAppBar(
            title = "주문",
            textStyle = TextStyle(fontSize = 18.sp),
            onNavigationIconClick = {
                if (isSearchMode) {
                    exitSearchMode()
                } else {
                    onNavigationClick()
                }
            },
            actions = {
                Icon(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(28.dp)
                        .noRippleClickable { onCartClick() },
                    painter = painterResource(id = R.drawable.ic_shopping_cart),
                    contentDescription = "장바구니"
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = KoinTheme.colors.info50
            )
        )

        if (!isSearchMode) {
            SearchBarFake(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                onClick = { isSearchMode = true }
            )
            CategoryChips(
                categories = categories,
                selectedIndex = selectedCategoryIndex,
                onCategorySelected = { idx ->
                    viewModel.setCategory(idx)
                    viewModel.refreshStores()
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            StoreFilterBar(
                viewModel = viewModel,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val pullRefreshState = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = { viewModel.refreshStores() },
                state = pullRefreshState
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
//                    .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AutoScrollingBanner(
                            storeEvents = storeEvents ?: emptyList(),
                            onItemClick = { }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    items(storeList) { store ->
                        StoreCard(
                            store = store,
                            imageUrl = R.drawable.ic_porkfeet.toString(),
                            modifier = Modifier.padding(horizontal = 24.dp),
                            onClick = {
                                val intent = Intent(context, StoreDetailActivity::class.java).apply {
                                    putExtra(StoreDetailActivityContract.STORE_ID, store.uid)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        } else {
            SearchBar(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    viewModel.updateSearchQuery(newQuery)
                    viewModel.getRelatedStore()
                    viewModel.searchStore()
                },
                onSearch = { viewModel.searchStore() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 16.dp)
                    .focusRequester(focusRequester)
            )
            if (searchQuery.isEmpty()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(KoinTheme.colors.neutral500)
                        .clickable { exitSearchMode() }
                )
            } else {
                if (searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        items(searchResults) { item ->
                            SearchResultItem(
                                item = item,
                                onClick = {
                                    val shopId = item.shopId ?: item.shopIds.firstOrNull()
                                    if (shopId != null) {
                                        val intent = Intent(context, StoreDetailActivity::class.java).apply {
                                            putExtra(StoreDetailActivityContract.STORE_ID, shopId)
                                        }
                                        context.startActivity(intent)
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(KoinTheme.colors.neutral500)
                            .clickable { exitSearchMode() }
                    )
                }
            }
        }
    }
}
