import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.DeliveryTipModel
import `in`.koreatech.koin.feature.store.model.OwnerInfoModel
import `in`.koreatech.koin.feature.store.model.StoreDescriptionModel
import `in`.koreatech.koin.feature.store.view.hasAnyInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopOriginInfoScreen(
    cartItemNumber: Int = 0,
    shopDescription: StoreDescriptionModel,
    onBackClick: () -> Unit,
    navigateToShoppingCart: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                ),
                title = { Text(stringResource(R.string.store_info_and_origin)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
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
                                    .background(Color.Magenta, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartItemNumber.toString(),
                                    fontSize = 10.sp,
                                    color = Color.White
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
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.store_info),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = shopDescription.notice ?: stringResource(R.string.no_registered_information), fontSize = 14.sp)
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.total_delivery_tip_by_order_amount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            DeliveryFeeTable(
                modifier = Modifier.fillMaxWidth(),
                deliveryFees = shopDescription.deliveryTips ?: emptyList()
            )
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.business_info),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (shopDescription.ownerInfo.hasAnyInfo()) {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (shopDescription.ownerInfo?.name != null) Text(text = stringResource(R.string.owner_name))
                        if (shopDescription.ownerInfo?.shopName != null) Text(text = stringResource(R.string.trade_name))
                        if (shopDescription.ownerInfo?.address != null) Text(text = stringResource(R.string.business_address))
                        if (shopDescription.ownerInfo?.companyRegistrationNumber != null) Text(stringResource(R.string.business_registration_number))
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        shopDescription.ownerInfo?.name?.let { Text(it) }
                        shopDescription.ownerInfo?.shopName?.let { Text(it) }
                        shopDescription.ownerInfo?.address?.let { Text(it) }
                        shopDescription.ownerInfo?.companyRegistrationNumber?.let { Text(it) }
                    }
                }
            } else {
                Text(text = stringResource(R.string.no_registered_information))
            }
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.origin_marking),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = shopDescription.origins?.joinToString(separator = ", ") {
                    "${it.ingredients} (${it.origin})"
                } ?: stringResource(R.string.no_registered_information),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun DeliveryFeeTable(
    modifier: Modifier = Modifier,
    deliveryFees: List<DeliveryTipModel>
) {
    Column(
        modifier = modifier
            .border(1.dp, Color.LightGray)
    ) {
        deliveryFees.forEach { fee ->
            DeliveryFeeRow(
                modifier = Modifier.fillMaxWidth(),
                label = if (fee.toAmount != null) {
                    stringResource(id = R.string.fee_range, fee.fromAmount ?: 0, fee.toAmount)
                } else {
                    stringResource(id = R.string.fee_range_start, fee.fromAmount ?: 0)
                },
                value = stringResource(id = R.string.price_with_won, fee.fee.toString())
            )
            HorizontalDivider(color = Color.LightGray)
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
fun ShopOriginInfoScreenPreview() {
    ShopOriginInfoScreen(
        onBackClick = {},
        navigateToShoppingCart = {},
        shopDescription = StoreDescriptionModel(
            id = 1,
            description = "테스트 가게 설명",
            storeName = "테스트 가게",
            notice = "테스트 가게 공지사항",
            deliveryTips = listOf(
                DeliveryTipModel(fromAmount = 0, toAmount = 12000, fee = 3000),
                DeliveryTipModel(fromAmount = 12000, toAmount = 22000, fee = 2500),
                DeliveryTipModel(fromAmount = 22000, toAmount = null, fee = 110)
            ),
            origins = null,
            ownerInfo = OwnerInfoModel(
                name = "홍길동",
                shopName = "테스트 가게",
                address = "서울시 강남구 역삼동 123-45",
                companyRegistrationNumber = "123-45-67890"
            )
        )
    )
}
