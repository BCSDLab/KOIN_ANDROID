package `in`.koreatech.koin.feature.store.cartadd

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.feature.store.DEEPLINK_STORE_ADD_CART
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.AddMenuBottomCard
import `in`.koreatech.koin.feature.store.component.KoinCartOptionItem
import `in`.koreatech.koin.feature.store.component.KoinCartPriceItem
import `in`.koreatech.koin.feature.store.component.KoinStoreDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.component.KoinStoreSignInDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.QuantitySelectorSection
import `in`.koreatech.koin.feature.store.enums.CartError.DIFFERENT_SHOP_ITEM_IN_CART
import `in`.koreatech.koin.feature.store.enums.CartError.INVALID_MENU_IN_SHOP
import `in`.koreatech.koin.feature.store.enums.CartError.MAX_SELECTION_EXCEEDED
import `in`.koreatech.koin.feature.store.enums.CartError.MENU_SOLD_OUT
import `in`.koreatech.koin.feature.store.enums.CartError.MIN_SELECTION_NOT_MET
import `in`.koreatech.koin.feature.store.enums.CartError.NONE
import `in`.koreatech.koin.feature.store.enums.CartError.REQUIRED_OPTION_GROUP_MISSING
import `in`.koreatech.koin.feature.store.enums.CartError.SHOP_CLOSED
import `in`.koreatech.koin.feature.store.model.LocalCartAdd
import `in`.koreatech.koin.feature.store.model.LocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.LocalShopPrice
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import kotlin.math.roundToInt
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAddScreen(
    viewModel: CartAddViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateBack: (Boolean) -> Unit = {}
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val navigator = rememberNavigator()

    viewModel.collectSideEffect {
        handleSideEffect(it, navigateBack)
    }

    val rememberState = rememberCollapsingToolbarState()
    val overlayAlpha = rememberState.progress()
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    if (uiState.showErrorDialog) {
        KoinStoreDialog(
            message = stringResource(uiState.error.message),
            onDismissRequest = viewModel::dismissErrorDialog
        ) {
            when (uiState.error) {
                DIFFERENT_SHOP_ITEM_IN_CART -> viewModel.resetCart()
                NONE,
                MENU_SOLD_OUT,
                REQUIRED_OPTION_GROUP_MISSING,
                MIN_SELECTION_NOT_MET,
                MAX_SELECTION_EXCEEDED,
                INVALID_MENU_IN_SHOP,
                SHOP_CLOSED -> viewModel.dismissErrorDialog()
            }
        }
    }

    if (uiState.showSignInDialog) {
        val cartData = Json.encodeToString(
            LocalCartAdd(
                orderableShopId = uiState.orderableShopId,
                orderableShopMenuId = uiState.orderableShopMenuId,
                orderableShopMenuPriceId = uiState.orderableShopMenuPriceId,
                orderableShopMenuOptionIds = uiState.orderableShopMenuOptionIds,
                quantity = uiState.quantity
            )
        )
        KoinStoreSignInDialog(
            onPositive = {
                navigator.navigateToSignIn(context, "$DEEPLINK_STORE_ADD_CART/${uiState.orderableShopId}/${uiState.orderableShopMenuId}/$cartData").apply {
                    context.startActivity(this)
                }
            },
            onNegative = viewModel::hideSignInDialog
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(4f),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
    }

    if (uiState.menuName.isEmpty()) return // If menu not loaded, do not render the screen

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .nestedScroll(nestedScrollConnection)
        ) {
            CartAddScreen(
                menuName = uiState.menuName,
                menuDescription = uiState.menuDescription,
                menuPrices = uiState.prices,
                menuOptions = uiState.options,
                orderableShopMenuPriceId = uiState.orderableShopMenuPriceId,
                quantity = uiState.quantity,
                onPriceSelected = viewModel::updateMenuPriceId,
                onSelectedOptionGroup = viewModel::updateSelectedOptionGroup,
                onQuantityChange = viewModel::updateQuantity,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = rememberState.toolbarMinHeight + statusBarHeight)
                    .offset {
                        IntOffset(
                            0,
                            currentToolbarHeightDp.value.toPx().roundToInt() + statusBarHeight.toPx().roundToInt()
                        )
                    }
            )

            KoinStoreTopAppBar(
                modifier = Modifier.zIndex(2f),
                title = uiState.menuName,
                onNavigationIconClick = {
                    navigateBack(false)
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = {
                            navigateToCart()
                        }) {
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
                overlayAlpha = overlayAlpha,
                expandedColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = RebrandKoinTheme.colors.neutral0,
                    titleContentColor = RebrandKoinTheme.colors.neutral0,
                    actionIconContentColor = if (uiState.menuImageUrls.firstOrNull() == null) RebrandKoinTheme.colors.neutral800 else RebrandKoinTheme.colors.neutral0
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(rememberState.toolbarMinHeight, rememberState.toolbarMaxHeight + statusBarHeight)
                        .graphicsLayer {
                            clip = true
                            translationY = -(rememberState.toolbarMaxHeight.toPx() - currentToolbarHeightDp.value.toPx())
                            alpha = 1f - overlayAlpha.value
                        }
                        .zIndex(1f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.menuImageUrls.firstOrNull()),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = 1f - overlayAlpha.value
                            }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        BasicText(
                            modifier = Modifier.graphicsLayer {
                                alpha = 1 - overlayAlpha.value
                            },
                            text = uiState.menuName,
                            style = RebrandKoinTheme.typography.bold20.copy(
                                color = RebrandKoinTheme.colors.neutral800
                            )
                        )

                        BasicText(
                            modifier = Modifier.graphicsLayer {
                                alpha = 1 - overlayAlpha.value
                            },
                            text = stringResource(R.string.price_with_won, uiState.prices.getOrNull(0)?.price ?: 0),
                            style = RebrandKoinTheme.typography.bold20.copy(
                                color = RebrandKoinTheme.colors.primary500
                            )
                        )
                        BasicText(
                            modifier = Modifier.graphicsLayer {
                                alpha = 1 - overlayAlpha.value
                            },
                            text = uiState.menuDescription,
                            style = RebrandKoinTheme.typography.regular12.copy(
                                color = RebrandKoinTheme.colors.neutral500
                            )
                        )
                    }
                }
            }
        }
        AddMenuBottomCard(
            price = uiState.price,
            isButtonEnabled = uiState.isButtonEnabled,
            onClick = {
                if (uiState.isLoggedIn) {
                    viewModel.addCartItem()
                } else {
                    viewModel.showSignInDialog()
                }
            }
        )
    }
}

@Composable
private fun CartAddScreen(
    menuName: String,
    menuDescription: String,
    menuPrices: List<LocalShopPrice>,
    menuOptions: List<LocalShopMenuOptionGroup>,
    quantity: Int,
    orderableShopMenuPriceId: Int,
    modifier: Modifier = Modifier,
    onPriceSelected: (Int) -> Unit = { },
    onSelectedOptionGroup: (Int, Int) -> Unit = { _, _ -> },
    onQuantityChange: (Int) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        KoinCartPriceItem(
            prices = menuPrices,
            title = menuName,
            description = menuDescription,
            selectedId = orderableShopMenuPriceId
        ) {
            onPriceSelected(it)
        }

        Spacer(modifier = Modifier.height(12.dp))

        menuOptions.forEach { localShopMenuOptionGroup ->
            KoinCartOptionItem(
                title = localShopMenuOptionGroup.name,
                options = localShopMenuOptionGroup.options,
                description = localShopMenuOptionGroup.description,
                selectedId = localShopMenuOptionGroup.options.flatMap {
                    localShopMenuOptionGroup.options.filter { option ->
                        option.optionSelected
                    }.map { it.id }
                },
                requiredSelectCount = if (localShopMenuOptionGroup.minSelect == localShopMenuOptionGroup.maxSelect) {
                    localShopMenuOptionGroup.minSelect
                } else {
                    0
                }
            ) { selectedItems ->
                onSelectedOptionGroup(localShopMenuOptionGroup.id, selectedItems)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            QuantitySelectorSection(
                value = quantity,
                onIncrement = {
                    if (quantity < 10) onQuantityChange(quantity + 1)
                },
                onDecrement = {
                    if (quantity > 1) onQuantityChange(quantity - 1)
                }
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: CartAddSideEffect,
    navigateBack: (Boolean) -> Unit
) {
    when (sideEffect) {
        CartAddSideEffect.CartItemAdded -> {
            navigateBack(true)
        }
    }
}
