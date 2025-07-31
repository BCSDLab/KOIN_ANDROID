package `in`.koreatech.koin.feature.store.view

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.viewmodel.StoreDetailViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopOriginInfoScreen(
    cartItemNumber: Int = 0,
    storeDetailViewModel: StoreDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    navigateToShoppingCart: () -> Unit = {}
) {
    val uiState = storeDetailViewModel.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                ),
                title = { Text(stringResource(R.string.store_info_and_origin)) },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .size(36.dp)
                            .noRippleClickable { onBackClick() },
                        imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                },
                actions = {
                    Box(contentAlignment = Alignment.TopEnd) {
                        IconButton(onClick = navigateToShoppingCart) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_shopping_cart),
                                contentDescription = null
                            )
                        }
                        if (cartItemNumber > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (-6).dp, y = (6).dp)
                                    .size(16.dp)
                                    .background(RebrandKoinTheme.colors.primary500, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartItemNumber.toString(),
                                    fontSize = 10.sp,
                                    color = KoinTheme.colors.neutral0
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.store_detail_background))
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.store_info),
                style = RebrandKoinTheme.typography.bold18
            )
            Text(text = uiState.value.shopDescription.notice ?: stringResource(R.string.no_registered_information), style = RebrandKoinTheme.typography.regular14)
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.total_delivery_tip_by_order_amount),
                style = RebrandKoinTheme.typography.bold18
            )
            DeliveryFeeTable(
                modifier = Modifier.fillMaxWidth(),
                deliveryFees = uiState.value.shopDescription.deliveryTips ?: emptyList()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.business_info),
                style = RebrandKoinTheme.typography.bold18
            )
            if (uiState.value.shopDescription.ownerInfo.hasAnyInfo()) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (uiState.value.shopDescription.ownerInfo?.name != null) Text(text = stringResource(R.string.owner_name))
                        if (uiState.value.shopDescription.ownerInfo?.shopName != null) Text(text = stringResource(R.string.trade_name))
                        if (uiState.value.shopDescription.ownerInfo?.address != null) Text(text = stringResource(R.string.business_address))
                        if (uiState.value.shopDescription.ownerInfo?.companyRegistrationNumber != null) Text(stringResource(R.string.business_registration_number))
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.value.shopDescription.ownerInfo?.name?.let { Text(it) }
                        uiState.value.shopDescription.ownerInfo?.shopName?.let { Text(it) }
                        uiState.value.shopDescription.ownerInfo?.address?.let { Text(it) }
                        uiState.value.shopDescription.ownerInfo?.companyRegistrationNumber?.let { Text(it) }
                    }
                }
            } else {
                Text(text = stringResource(R.string.no_registered_information))
            }
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.origin_marking),
                style = RebrandKoinTheme.typography.bold18
            )
            Text(
                text = uiState.value.shopDescription.origins?.joinToString(separator = ", ") {
                    "${it.ingredients} (${it.origin})"
                } ?: stringResource(R.string.no_registered_information),
                style = RebrandKoinTheme.typography.regular14
            )
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

@Preview(showBackground = true)
@Composable
private fun ShopOriginInfoScreenPreview() {
    ShopOriginInfoScreen(
        onBackClick = {},
        navigateToShoppingCart = {}

    )
}
