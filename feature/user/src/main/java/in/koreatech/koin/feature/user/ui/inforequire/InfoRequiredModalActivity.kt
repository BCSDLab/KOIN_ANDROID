package `in`.koreatech.koin.feature.user.ui.inforequire

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.userinfo.UserInfoActivity

class InfoRequiredModalActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                InfoRequiredModalScreen(
                    title = stringResource(R.string.info_required_modal_title),
                    description = stringResource(R.string.info_required_modal_description),
                    descriptionStyle =
                        KoinTheme.typography.regular12.copy(
                            textAlign = TextAlign.Center,
                            color = KoinTheme.colors.neutral500
                        ),
                    onPositive = {
                        val intent = Intent(this@InfoRequiredModalActivity, UserInfoActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0) // deprecated : 아직 대체할수 있는 기능이 없음..
                    },
                    onNegative = {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                )
            }
        }
    }
}