package `in`.koreatech.koin.storev2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.pager.rememberPagerState
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.databinding.ActivityStoreDetailBinding
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.feature.store.view.ShoppingCartScreen
import `in`.koreatech.koin.feature.store.view.StoreDetailScreen

@AndroidEntryPoint
class ShoppingCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            KoinTheme {
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
}
