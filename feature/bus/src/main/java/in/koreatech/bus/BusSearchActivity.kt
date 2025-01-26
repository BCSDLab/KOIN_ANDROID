package `in`.koreatech.bus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.bus.navigation.BusSearchNavigation
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@AndroidEntryPoint
class BusSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinTheme(
                isLightStatusBar = true
            ) {
                BusSearchNavigation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}