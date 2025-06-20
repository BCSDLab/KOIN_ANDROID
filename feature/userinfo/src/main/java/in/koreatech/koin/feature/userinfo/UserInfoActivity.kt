package `in`.koreatech.koin.feature.userinfo

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.userinfo.ui.userinfoedit.UserInfoEditScreen

@AndroidEntryPoint
class UserInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                UserInfoEditScreen()
            }
        }
    }
}
