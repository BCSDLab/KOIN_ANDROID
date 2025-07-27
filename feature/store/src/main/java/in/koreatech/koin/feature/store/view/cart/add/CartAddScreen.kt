package `in`.koreatech.koin.feature.store.view.cart.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.feature.store.util.CustomClosingToolbarScreenDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.AddMenuBottomCard
import `in`.koreatech.koin.feature.store.component.KoinCartOptionItem
import `in`.koreatech.koin.feature.store.component.KoinCartPriceItem
import `in`.koreatech.koin.feature.store.component.KoinStoreDialog
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.model.LocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.LocalShopPrice
import `in`.koreatech.koin.feature.store.scroll.storeCollapsingToolbarConnection
import `in`.koreatech.koin.feature.store.state.currentToolbarHeightDp
import `in`.koreatech.koin.feature.store.state.progress
import `in`.koreatech.koin.feature.store.state.rememberCollapsingToolbarState
import kotlin.math.roundToInt
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAddScreen(
    viewModel: CartAddViewModel = hiltViewModel(),
    navigateToCart: () -> Unit = {},
    navigateToBack: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    val rememberState = rememberCollapsingToolbarState(
        toolbarMinHeight = 64.dp
    )
    val progress = rememberState.progress()
    val overlayAlpha = (progress).coerceIn(0f, 1f)
    val nestedScrollConnection = storeCollapsingToolbarConnection(
        listState = rememberState.listState,
        toolbarOffsetPx = rememberState.toolbarOffsetPx,
        toolbarHeightPx = rememberState.toolbarHeightPx,
        minHeightPx = rememberState.minHeightPx
    )
    val currentToolbarHeightDp = rememberState.currentToolbarHeightDp()

    if (uiState.showErrorDialog) {
        KoinStoreDialog(
            message = stringResource(uiState.error.message),
            onDismissRequest = viewModel::dismissErrorDialog,
        )
    }

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
                onPriceSelected = viewModel::updateMenuPriceId,
                onSelectedOptionGroup = viewModel::updateSelectedOptionGroup,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = currentToolbarHeightDp + CustomClosingToolbarScreenDefaults.windowInsets.asPaddingValues().calculateTopPadding())
            )

            KoinStoreTopAppBar(
                modifier = Modifier.zIndex(2f),
                title = uiState.menuName,
                onNavigationIconClick = {
                    navigateToBack()
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = {
                            navigateToCart()
                        }) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null,
                                tint = lerp(
                                    RebrandKoinTheme.colors.neutral800,
                                    RebrandKoinTheme.colors.neutral0,
                                    1f - overlayAlpha
                                )
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
                    containerColor = RebrandKoinTheme.colors.neutral0.copy(alpha = overlayAlpha),
                    actionIconContentColor = lerp(RebrandKoinTheme.colors.neutral800, RebrandKoinTheme.colors.neutral0, 1f - overlayAlpha),
                    titleContentColor = RebrandKoinTheme.colors.neutral800.copy(alpha = overlayAlpha)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        rememberState.toolbarMaxHeight + CustomClosingToolbarScreenDefaults.windowInsets
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .offset { IntOffset(0, rememberState.toolbarOffsetPx.floatValue.roundToInt()) }
                    .zIndex(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uiState.menuImageUrls.firstOrNull()),
                    contentDescription = null,
                    alpha = 1 - overlayAlpha,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    BasicText(
                        text = uiState.menuName,
                        style = RebrandKoinTheme.typography.bold20.copy(
                            color = RebrandKoinTheme.colors.neutral800.copy(
                                alpha = 1 - overlayAlpha
                            )
                        )
                    )

                    BasicText(
                        text = stringResource(R.string.price_with_won, uiState.prices.getOrNull(0)?.price ?: 0),
                        style = RebrandKoinTheme.typography.bold20.copy(
                            color = RebrandKoinTheme.colors.primary500.copy(
                                alpha = 1 - overlayAlpha
                            )
                        )
                    )
                    BasicText(
                        text = uiState.menuDescription,
                        style = RebrandKoinTheme.typography.regular12.copy(
                            color = RebrandKoinTheme.colors.neutral500.copy(
                                alpha = 1 - overlayAlpha
                            )
                        )
                    )
                }
            }
        }
        AddMenuBottomCard(
            price = uiState.price,
            isButtonEnabled = uiState.isButtonEnabled,
            onClick = { viewModel.addCartItem() }
        )
    }
}

@Composable
private fun CartAddScreen(
    menuName: String,
    menuDescription: String,
    menuPrices: List<LocalShopPrice>,
    menuOptions: List<LocalShopMenuOptionGroup>,
    orderableShopMenuPriceId: Int,
    modifier: Modifier = Modifier,
    onPriceSelected: (Int) -> Unit = { },
    onSelectedOptionGroup: (Int, Int) -> Unit = { _, _ -> }
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
                minSelectCount = localShopMenuOptionGroup.minSelect
            ) { selectedItems ->
                onSelectedOptionGroup(localShopMenuOptionGroup.id, selectedItems)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
