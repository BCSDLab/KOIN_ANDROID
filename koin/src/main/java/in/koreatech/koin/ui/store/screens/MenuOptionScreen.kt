package `in`.koreatech.koin.ui.store.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.ui.store.components.BottomFloatingLayout
import `in`.koreatech.koin.ui.store.components.MenuImageSection
import `in`.koreatech.koin.ui.store.components.MenuInfoSection
import `in`.koreatech.koin.ui.store.components.MenuOption
import `in`.koreatech.koin.ui.store.components.MenuOptionsSection
import `in`.koreatech.koin.ui.store.components.OptionItem
import `in`.koreatech.koin.ui.store.components.QuantitySelectorSection
import `in`.koreatech.koin.ui.store.components.SelectionType

@Composable
fun MenuOptionScreen() {
    // API 완성 후 수정 예정
    val menuName = "족발 + 막국수 저녁 Set"
    val menuPrice = 18000
    val menuDetail = "메뉴 세부사항 구성 등등"
    val options = listOf(
        MenuOption(
            name = "사이즈",
            description = "옵션 설명 등등등(선택사항)",
            selectionType = SelectionType.RADIO,
            required = true,
            selectCount = 1,
            items = listOf(
                OptionItem("소", 25000),
                OptionItem("중", 25000),
                OptionItem("대", 45000)
            )
        ),
        MenuOption(
            name = "맛",
            description = "",
            selectionType = SelectionType.CHECKBOX,
            required = false,
            selectCount = 2,
            items = listOf(
                OptionItem("맛있는맛", 0),
                OptionItem("더 맛있는맛", 3500),
                OptionItem("매운맛", 3500)
            )
        )
    )
    var quantity by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomFloatingLayout(
                text = "장바구니 추가",
                price = menuPrice * quantity,
                onClick = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 0.dp, bottom = 0.dp)
        ) {
            item {
                MenuImageSection(imageResInt = R.drawable.no_image)
            }
            item {
                MenuInfoSection(menuName, menuPrice, menuDetail)
            }
            item {
                MenuOptionsSection(options)
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    QuantitySelectorSection(
                        value = quantity,
                        borderColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.primary500,
                        onIncrement = { quantity++ },
                        onDecrement = { if (quantity > 1) quantity-- }
                    )
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun MenuOptionScreenPreview() {
    MenuOptionScreen()
}
