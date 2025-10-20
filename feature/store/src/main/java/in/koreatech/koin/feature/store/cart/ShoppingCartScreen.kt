package `in`.koreatech.koin.feature.store.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.cart.component.ShoppingCartContent
import `in`.koreatech.koin.feature.store.cart.component.ShoppingCartEmptyContent
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.OrderBottomBar
import `in`.koreatech.koin.feature.store.enums.CartValidation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    isCartModified: Boolean = false,
    viewModel: ShoppingCartViewModel = hiltViewModel(),
    isOperating: Boolean = true,
    navigateToStoreDetail: (Int) -> Unit = { },
    navigateToPayment: (String) -> Unit = { },
    navigateToCartEdit: (Int) -> Unit = { },
    navigateToStoreMain: () -> Unit = { },
    navigateBack: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        snapshotFlow { uiState.cart }
            .collect {
                if (it.shopName != null) {
                    viewModel.getCartValidate()
                }
            }
    }

    LaunchedEffect(isCartModified) {
        snapshotFlow { isCartModified }
            .distinctUntilChanged()
            .collectLatest {
                if (it) {
                    viewModel.getCart(uiState.cartType)
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
    }

    Scaffold(
        topBar = {
            KoinStoreTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                ),
                title = stringResource(R.string.shopping_cart),
                onNavigationIconClick = {
                    navigateBack()
                },
                actions = {
                    Text(
                        color = if (uiState.cart.items.isEmpty()) RebrandKoinTheme.colors.primary300 else RebrandKoinTheme.colors.primary500,
                        fontWeight = SemiBold,
                        text = stringResource(R.string.delete_all),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .let {
                                if (uiState.cart.items.isEmpty()) {
                                    it
                                } else {
                                    it.noRippleClickable { viewModel.setShowDeleteDialog(true) }
                                }
                            }
                    )
                }
            )
        },
        bottomBar = {
            if (uiState.cart.items.isNotEmpty()) {
                OrderBottomBar(
                    itemCount = uiState.totalItemCount,
                    totalPrice = uiState.cart.totalAmount,
                    isOrderEnabled = uiState.cartValidation == CartValidation.VALID,
                    orderableMessage = if (uiState.cart.totalAmount >= uiState.minimumOrderAmount) {
                        stringResource(R.string.store_cart_amount_met)
                    } else {
                        stringResource(R.string.store_cart_amount_not_met, uiState.minimumOrderAmount - uiState.cart.totalAmount)
                    },
                    navigateToCart = {
                        navigateToPayment(uiState.cartType.name)
                    }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.cart.items.isEmpty()) {
            ShoppingCartEmptyContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                navigateToStoreMain = navigateToStoreMain
            )
            return@Scaffold
        }
        ShoppingCartContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            cart = uiState.cart,
            cartType = uiState.cartType,
            isOperating = isOperating,
            dialogVisibility = uiState.showDeleteDialog,
            onOrderModeChanged = { viewModel.getCart(it) },
            onChangeQuantity = { cartMenuItemId, quantity ->
                viewModel.resetCartValidation()
                viewModel.modifyCartMenuQuantity(cartMenuItemId, quantity)
            },
            onResetMenu = {
                viewModel.resetCart()
            },
            deleteCartMenuItem = { cartMenuItemId ->
                viewModel.deleteCartMenuItem(cartMenuItemId)
            },
            setDialogVisibility = {
                viewModel.setShowDeleteDialog(it)
            },
            navigateToCartEdit = navigateToCartEdit,
            navigateToStoreDetail = { storeId ->
                navigateToStoreDetail(storeId)
            }
        )
    }
}

@Composable
@Preview
private fun ShoppingCartItem() {
    KoinTheme {
        Column {
            ShoppingCartScreen(
                navigateToStoreDetail = {}
            )
        }
    }
}
