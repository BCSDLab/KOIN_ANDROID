package `in`.koreatech.koin.feature.banner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar

@AndroidEntryPoint
class BannerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeWithDarkStatusBar()

        setContent {
        }
    }
}