package `in`.koreatech.bus

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.bus.navigation.BusTimetableNavigation
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@AndroidEntryPoint
class BusTimetableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinTheme {
                BusTimetableNavigation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}