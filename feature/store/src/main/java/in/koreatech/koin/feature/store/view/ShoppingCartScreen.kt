package `in`.koreatech.koin.feature.store.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.feature.store.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    onBackClick: () -> Unit,
    onDeleteAllClick: () -> Unit,
    storeInfo: StoreDetailInfo,
    isEmpty: Boolean
) {
    Scaffold(
        topBar = {
            KoinTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.store_detail_background)
                ),
                title = stringResource(R.string.shopping_cart),
                onNavigationIconClick = onBackClick,
                actions = {
                    Text(
                        color = RebrandKoinTheme.colors.primary500,
                        fontWeight = SemiBold,
                        text = stringResource(R.string.delete_all),
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .noRippleClickable { onDeleteAllClick() }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (isEmpty) {
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
            storeInfo = storeInfo
        )
    }
}

@Composable
@Preview
private fun ShoppingCartItem() {
    KoinTheme {
        Column {
            ShoppingCartScreen(
                onBackClick = {},
                onDeleteAllClick = {},
                storeInfo = StoreDetailInfo(
                    address = "서울시 강남구 역삼동 123-45",
                    name = "테스트 가게",
                    description = "테스트 가게 설명",
                    imageUrls = listOf("https://example.com/image.jpg"),
                    mainCategoryId = 1,
                    categoryIds = listOf(1, 2, 3),
                    isBankOk = true,
                    isCardOk = true,
                    isDeliveryOk = true,
                    phone = "010-1234-5678",
                    accountNumber = "123-456-7890",
                    deliveryPrice = 3000,
                    operatingTime = null,
                    bank = "우리은행"
                ),
                isEmpty = false
            )
        }
    }
}
