package `in`.koreatech.koin.feature.store.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.viewmodel.ShoppingCartViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel = hiltViewModel(),
    isOperating: Boolean = true,
    navigateToStoreDetail: () -> Unit = { }
) {
    val uiState by viewModel.collectAsState()

    Scaffold(
        topBar = {
            KoinTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                ),
                title = stringResource(R.string.shopping_cart),
                onNavigationIconClick = navigateToStoreDetail,
                actions = {
                    Text(
                        color = if (uiState.cart.items.isEmpty()) RebrandKoinTheme.colors.primary300 else RebrandKoinTheme.colors.primary500,
                        fontWeight = SemiBold,
                        text = stringResource(R.string.delete_all),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .noRippleClickable {
                                if (uiState.cart.items.isEmpty()) {
                                    return@noRippleClickable
                                }
                                viewModel.setShowDeleteDialog(true)
                            }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (uiState.cart.items.isEmpty()) {
            ShoppingCartEmptyContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
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
