package `in`.koreatech.koin.storev2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.databinding.ActivityStoreDetailBinding
import `in`.koreatech.koin.feature.store.detail.StoreDetailScreen

@AndroidEntryPoint
class StoreDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        initComposeView()
    }

    private fun initComposeView() {
        binding.composeView.setContent {
            KoinTheme {
                StoreDetailScreen()
            }
        }
    }
}
