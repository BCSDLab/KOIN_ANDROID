package `in`.koreatech.koin.storev2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.core.view.WindowCompat
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.databinding.ActivityStoreDetailBinding
import `in`.koreatech.koin.domain.model.owner.MenuCategory
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.feature.store.component.CustomCollapsingToolbarScreen

class StoreDetailActivity : AppCompatActivity() {
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
            }
        }
    }
}