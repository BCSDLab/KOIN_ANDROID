package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.ui.screens.store.StoreMainScreen
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract

@AndroidEntryPoint
class StoreActivity : ComponentActivity() {
    // 나중에 제거
    interface StoreCategoryFactory {
        fun getCurrentCategory(): String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val categoryId = intent.getIntExtra(StoreActivityContract.STORE_CATEGORY, 0)
        setContent {
            StoreMainScreen(
                categoryId = categoryId
            )
        }
    }
}
