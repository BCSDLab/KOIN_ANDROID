package `in`.koreatech.koin.feature.store.orders

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_MAIN_HOME
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.orders.component.FilterOverlay
import `in`.koreatech.koin.feature.store.orders.component.OrdersTabRow
import `in`.koreatech.koin.feature.store.orders.screen.OrderHistoryScreen
import `in`.koreatech.koin.feature.store.orders.screen.OrderInProgressScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = { },
    navigateToReview: (Int) -> Unit = { },
    navigateToReorder: (Int) -> Unit = { },
    onBackPressed: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onSearchCancel()
            viewModel.resetFilter()
        }
    }

    BackHandler(
        enabled = uiState.isSearching
    ) {
        viewModel.onSearchCancel()
    }

    viewModel.collectSideEffect {
        handleSideEffect(it, navigateToCart)
    }

    LaunchedEffect(Unit) {
        viewModel.getUserType()

        snapshotFlow { uiState.filters }
            .collect {
                viewModel.getNewOrderHistoryData()
            }
        viewModel.getOrderInProgressData()
    }

    if (uiState.showSignInDialog) {
        KoinStoreSignInDialog(
            onPositive = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "koin://login/login?link=$DEEPLINK_STORE_MAIN_HOME".toUri()
                }.apply {
                    context.startActivity(this)
                }
            },
            onNegative = viewModel::hideSignInDialog
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            KoinStoreTopAppBar(
                title = stringResource(R.string.store_title_home_history),
                onNavigationIconClick = {
                    onBackPressed()
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = viewModel::navigateToCart) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null
                            )
                        }
                        if (uiState.cartItemCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (-5).dp, y = 5.dp)
                                    .size(16.dp)
                                    .background(RebrandKoinTheme.colors.primary500, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${uiState.cartItemCount}",
                                    style = RebrandKoinTheme.typography.medium12.copy(
                                        color = RebrandKoinTheme.colors.neutral0,
                                        lineHeightStyle = LineHeightStyle(
                                            trim = LineHeightStyle.Trim.Both,
                                            alignment = LineHeightStyle.Alignment.Center
                                        )
                                    )
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                )
            )

            OrdersTabRow(
                title = listOf(
                    stringResource(R.string.order_history_topbar),
                    stringResource(R.string.order_ongoing_topbar)
                ),
                selectedTabIndex = uiState.selectedTabIndex,
                onSelected = viewModel::onTabSelected
            )

            when (uiState.selectedTabIndex) {
                0 ->
                    OrderHistoryScreen(
                        filters = uiState.filters,
                        orderHistories = uiState.orderHistories,
                        isSearching = uiState.isSearching,
                        searchQuery = uiState.searchQuery,
                        getOrderHistoryData = viewModel::getOrderHistoryData,
                        onSearchStart = viewModel::onSearchStart,
                        onSearchCancel = viewModel::onSearchCancel,
                        onSearchDone = viewModel::onSearchDone,
                        onQueryChanged = viewModel::onSearchQueryChanged,
                        openFilterOverlay = viewModel::openFilterOverlay,
                        resetFilter = viewModel::resetFilter,
                        onWriteReviewClick = navigateToReview,
                        onReorderClick = navigateToReorder
                    )
                1 ->
                    OrderInProgressScreen(
                        orderInProgress = uiState.orderInProgress,
                        toOrderHistories = viewModel::onTabSelected
                    )
            }
        }

        if (uiState.isFilterSelecting) {
            FilterOverlay(
                filters = uiState.filters,
                onClose = viewModel::closeFilterOverlay,
                onApply = viewModel::applyFilter
            )
        }
    }
}

private fun handleSideEffect(
    sideEffect: OrderSideEffect,
    navigateToCart: () -> Unit
) {
    when (sideEffect) {
        OrderSideEffect.NavigateToCart -> {
            navigateToCart()
        }
    }
}
