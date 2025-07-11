package `in`.koreatech.koin.feature.user.ui.inforequire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.userinfo.UserInfoActivity
import androidx.core.content.edit

class InfoRequiredFullActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("info_required", Context.MODE_PRIVATE)
        prefs.edit { putInt("isShownBefore", 0) }

        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                InfoRequiredFullScreen(
                    lottieRes = R.raw.new_koin_lottie,
                    buttonText = stringResource(R.string.info_required_full_button),
                    titleText = stringResource(R.string.info_required_full_title),
                    descriptionText = stringResource(R.string.info_required_full_description),
                    onPositive = {
                        val intent = Intent(this@InfoRequiredFullActivity, UserInfoActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                )
            }
        }
    }
}
