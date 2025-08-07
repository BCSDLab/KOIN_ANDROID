package `in`.koreatech.koin.feature.store.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreTopAppBar
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import `in`.koreatech.koin.feature.store.enums.StoreDetailInfoType
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopOriginInfoScreen(
    selectedInfo: String,
    cartItemNumber: Int = 0,
    storeDetailViewModel: StoreDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navigateToShoppingCart: () -> Unit = {}
) {
    val uiState by storeDetailViewModel.collectAsState()

    LaunchedEffect(Unit) {
        storeDetailViewModel.getCartItemsCount()
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }
    }
    Scaffold(
        topBar = {
            KoinStoreTopAppBar(
                title = stringResource(R.string.store_info_and_origin),
                onNavigationIconClick = {
                    onBackClick()
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = {
                            navigateToShoppingCart()
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.store_detail_background))
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HighlightSection(isHighlighted = selectedInfo == StoreDetailInfoType.DETAIL.name) {
                Text(
                    text = stringResource(R.string.store_info),
                    style = RebrandKoinTheme.typography.bold18
                )
                Text(text = uiState.shopDescription.notice ?: stringResource(R.string.no_registered_information), style = RebrandKoinTheme.typography.regular14)
            }
            Spacer(Modifier.height(24.dp))
            HighlightSection(
                isHighlighted = selectedInfo == StoreDetailInfoType.DELIVERY.name
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.total_delivery_tip_by_order_amount),
                    style = RebrandKoinTheme.typography.bold18
                )
                DeliveryFeeTable(
                    modifier = Modifier.fillMaxWidth(),
                    deliveryFees = uiState.shopDescription.deliveryTips ?: emptyList()
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.business_info),
                style = RebrandKoinTheme.typography.bold18
            )
            if (uiState.shopDescription.ownerInfo.hasAnyInfo()) {
                Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = stringResource(R.string.owner_name))
                        Text(text = stringResource(R.string.trade_name))
                        Text(text = stringResource(R.string.business_address))
                        Text(stringResource(R.string.business_registration_number))
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.shopDescription.ownerInfo?.name?.let { Text(it) }
                        uiState.shopDescription.ownerInfo?.shopName?.let { Text(it) }
                        uiState.shopDescription.ownerInfo?.address?.let { Text(it) }
                        uiState.shopDescription.ownerInfo?.companyRegistrationNumber?.let { Text(it) }
                    }
                }
            } else {
                Text(text = stringResource(R.string.no_registered_information))
            }
            Spacer(Modifier.height(24.dp))
            HighlightSection(isHighlighted = selectedInfo == StoreDetailInfoType.ORIGIN.name) {
                Text(
                    text = stringResource(R.string.origin_marking),
                    style = RebrandKoinTheme.typography.bold18
                )
                Text(
                    text = uiState.shopDescription.origins?.joinToString(separator = ", ") {
                        "${it.ingredients} (${it.origin})"
                    } ?: stringResource(R.string.no_registered_information),
                    style = RebrandKoinTheme.typography.regular14
                )
            }
        }
    }
}

@Composable
fun DeliveryFeeTable(
    deliveryFees: List<DeliveryTipModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(1.dp, RebrandKoinTheme.colors.neutral400)
    ) {
        deliveryFees.forEach { fee ->
            DeliveryFeeRow(
                modifier = Modifier.fillMaxWidth(),
                label = if (fee.toAmount != null) {
                    stringResource(id = R.string.fee_range, fee.fromAmount ?: 0, fee.toAmount)
                } else {
                    stringResource(id = R.string.fee_range_start, fee.fromAmount ?: 0)
                },
                value = stringResource(id = R.string.price_with_won, fee.fee ?: 0)
            )
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral400)
        }
    }
}

@Composable
fun DeliveryFeeRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value)
    }
}

@Composable
fun HighlightSection(
    isHighlighted: Boolean,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable () -> Unit
) {
    var targetColor by remember { mutableStateOf(Color.Transparent) }
    val highlightColor = RebrandKoinTheme.colors.neutral400.copy(alpha = 0.3f)
    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    LaunchedEffect(isHighlighted, isLoading) {
        if (isHighlighted && !isLoading) {
            targetColor = highlightColor
            delay(800)
            targetColor = Color.Transparent
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedBackgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ShopOriginInfoScreenPreview() {
    ShopOriginInfoScreen(
        selectedInfo = StoreDetailInfoType.ORIGIN.name,
        onBackClick = {},
        navigateToShoppingCart = {}

    )
}
