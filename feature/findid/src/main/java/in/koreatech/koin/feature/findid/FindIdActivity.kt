package `in`.koreatech.koin.feature.findid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar

class FindIdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
            }
        }
    }
}
