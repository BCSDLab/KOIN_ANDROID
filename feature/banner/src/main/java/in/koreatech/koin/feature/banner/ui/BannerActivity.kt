package `in`.koreatech.koin.feature.banner.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar
import `in`.koreatech.koin.feature.banner.component.BannerA

@AndroidEntryPoint
class BannerActivity : ComponentActivity() {
    private val viewModel: BannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeWithDarkStatusBar()

        setContent {
            val uiState by viewModel.bannerState.collectAsState()

            KoinTheme {
                if (!uiState.isLoading) {
                    if (uiState.bannerList.isEmpty()) { // Finish activity if banner list is empty
                        finishActivity()
                        return@KoinTheme
                    }
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.Transparent,
                        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
                    ) { contentPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding)
                        ) {
                            BannerA(
                                bannerList = uiState.bannerList,
                                currentKoinVersion = uiState.currentVersionCode,
                                dismiss = {
                                    finishActivity()
                                },
                                dismissWithRefusal = {
                                    viewModel.setBannerRefusal()
                                    finishActivity()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    private fun finishActivity() {
        finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
    }
}
